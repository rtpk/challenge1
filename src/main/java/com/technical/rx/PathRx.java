package com.technical.rx;

import com.technical.file.NodePath;
import com.technical.node.NodeIterable;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public final class PathRx {

    public static Observable<WatchEvent<?>> watch(final Path path) throws IOException, InterruptedException {
        if (Files.isDirectory(path))
            return new ObservableFactory(path).create();
        else throw new IllegalArgumentException();
    }

    private static class ObservableFactory implements AutoCloseable {

        private final WatchService watcher;
        private final Path directory;
        private final ConcurrentMap<WatchKey, Path> directoriesByKey = new ConcurrentHashMap<>();
        private final Queue<Subscriber> subscriberQueue = new ConcurrentLinkedQueue<>();
        private final ExecutorService executor = Executors.newSingleThreadExecutor();
        private boolean errorFree = true;
        private Future task;
        private AtomicReference<Consumer<Subscriber>> addSubscriber = new AtomicReference<>(subscriberQueue::add);

        private ObservableFactory(final Path path) throws IOException, InterruptedException {
            final FileSystem fileSystem = path.getFileSystem();
            watcher = fileSystem.newWatchService();
            directory = path;
            startAddAllToWatcher();
            task = executor.submit(this::watchFolders);
        }

        private void watchFolders() {
            while (errorFree) {
                final WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException exception) {
                    subscriberQueue.forEach(s -> s.onError(exception));
                    errorFree = false;
                    break;
                }
                final Path dir = directoriesByKey.getOrDefault(key, directory);
                for (final WatchEvent<?> event : key.pollEvents()) {
                    subscriberQueue.forEach(s -> s.onNext(event));
                    addNewPathToWatcher(dir, event);
                }
                boolean valid = key.reset();
                if (!valid) {
                    directoriesByKey.remove(key);
                    if (directoriesByKey.isEmpty()) {
                        break;
                    }
                }
            }
            if (errorFree) {
                subscriberQueue.forEach(Observer::onCompleted);
            }
        }

        private Observable<WatchEvent<?>> create() {
            return Observable.create(p ->
                    addSubscriber.get().accept(p));
        }


        private void startAddAllToWatcher() throws InterruptedException {
            try {
                addAllPathsByIteratorToWatcher(directory);
            } catch (IOException exception) {
                subscriberQueue.forEach(s -> s.onError(exception));
                errorFree = false;
            }
        }

        private void addAllPathsByIteratorToWatcher(final Path rootDirectory) throws IOException, InterruptedException {
            NodeIterable<Path> root = new NodeIterable<>(new NodePath(rootDirectory));
            Observable<Path> result = Observable.from(root);
            result.filter(element -> Files.isDirectory(element)).forEach((dir) -> {
                try {
                    addPathToWatcher(dir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        private void addPathToWatcher(final Path dir) throws IOException {
            final WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            directoriesByKey.putIfAbsent(key, dir);
        }

        private void addNewPathToWatcher(final Path dir, final WatchEvent<?> event) {
            final Kind<?> kind = event.kind();
            if (kind.equals(ENTRY_CREATE)) {
                try {
                    if (Files.isDirectory(dir, NOFOLLOW_LINKS)) {
                        addPathToWatcher(dir);
                    }
                } catch (final IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        @Override
        public void close() throws Exception {
            addSubscriber.getAndSet((it) -> { });
            task.cancel(false);
            watcher.close();
            directoriesByKey.clear();
            subscriberQueue.clear();
        }


    }
}