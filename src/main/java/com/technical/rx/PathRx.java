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

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public final class PathRx {

    private PathRx() {
    }

    public static Observable<WatchEvent<?>> watch(final Path path) throws IOException {
        return new ObservableFactory(path).create();
    }

    private static class ObservableFactory {

        private final WatchService watcher;
        private final Path directory;
        private final ConcurrentMap<WatchKey, Path> directoriesByKey = new ConcurrentHashMap<>();
        private final Queue<Subscriber> subscriberList = new ConcurrentLinkedQueue<>();
        boolean errorFree = true;

        private ObservableFactory(final Path path) throws IOException {
            final FileSystem fileSystem = path.getFileSystem();
            watcher = fileSystem.newWatchService();
            directory = path;
            takeWatcher();
        }

        private void takeWatcher() {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {

                while (errorFree) {
                    final WatchKey key;
                    try {
                        key = watcher.take();
                    } catch (InterruptedException exception) {
                        subscriberList.forEach(s -> s.onError(exception));
                        errorFree = false;
                        break;
                    }
                    final Path dir = directoriesByKey.get(key);
                    for (final WatchEvent<?> event : key.pollEvents()) {
                        subscriberList.forEach(s -> s.onNext(event));
                        registerNewDirectory(dir, event);
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
                    subscriberList.forEach(Observer::onCompleted);
                }
            });
            executor.shutdown();
        }

        private Observable<WatchEvent<?>> create() {
            return Observable.create(subscriber -> {
                subscriberList.add(subscriber);
                try {
                    registerAll(directory);
                } catch (IOException exception) {
                    subscriberList.forEach(s -> s.onError(exception));
                    errorFree = false;
                }
            });
        }

        private void registerAll(final Path rootDirectory) throws IOException {
            NodeIterable<Path> root = new NodeIterable<>(new NodePath(rootDirectory));
            NodeIterableRx<Path> temp = new NodeIterableRx<>();
            Observable<Path> result = temp.convert(root);
            result.filter(element -> Files.isDirectory(element)).forEach((dir) -> {
                try {
                    register(dir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        private void register(final Path dir) throws IOException {
            final WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            if (!directoriesByKey.containsKey(key))
                directoriesByKey.put(key, dir);
        }


        @SuppressWarnings("unchecked")
        static <T> WatchEvent<T> cast(WatchEvent<?> event) {
            return (WatchEvent<T>) event;
        }

        private void registerNewDirectory(final Path dir, final WatchEvent<?> event) {
            final Kind<?> kind = event.kind();
            if (kind.equals(ENTRY_CREATE)) {
                final WatchEvent<Path> eventWithPath = cast(event);
                final Path name = eventWithPath.context();
                final Path child = dir.resolve(name);
                try {
                    if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                        register(child);
                    }
                } catch (final IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}