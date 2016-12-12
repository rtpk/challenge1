package com.technical.rx;

import com.technical.file.NodePath;
import com.technical.node.NodeIterable;
import org.springframework.stereotype.Service;
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

import static java.nio.file.StandardWatchEventKinds.*;

@Service
public class PathRx implements AutoCloseable {

    private final WatchService watcher;
    private final Path directory;
    private final ConcurrentMap<WatchKey, Path> directoriesByKey = new ConcurrentHashMap<>();
    private final Queue<Subscriber<? super WatchEvent<?>>> subscriberQueue = new ConcurrentLinkedQueue<>();
    private boolean errorFree = true;
    private Future task;
    private AtomicReference<Consumer<Subscriber>> addSubscriber = new AtomicReference<>(subscriberQueue::add);

    public PathRx(final Path path) throws IOException, InterruptedException {
        final FileSystem fileSystem = path.getFileSystem();
        watcher = fileSystem.newWatchService();
        directory = path;
        startAddAllToWatcher();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        task = executor.submit(this::watchFolders);
    }

    public Observable<WatchEvent<?>> watch() throws IOException, InterruptedException {
        if (Files.isDirectory(directory))
            return create();
        else throw new IllegalArgumentException();
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
                try {
                    addNewPathToWatcher(dir, event);
                } catch (IOException e) {
                    subscriberQueue.forEach(s -> s.onError(e));
                }
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

    public Observable<WatchEvent<?>> create() {
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
        addPathToWatcher(directory);

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
        //tutaj modifikacja zeby rejestrowalo potomkow
        directoriesByKey.putIfAbsent(key, dir);
    }

    private void addNewPathToWatcher(final Path dir, final WatchEvent<?> event) throws IOException {
        final Kind<?> kind = event.kind();
        if (kind.equals(ENTRY_CREATE)) {
            final WatchEvent<Path> eventWithPath = (WatchEvent<Path>) event;
            final Path name = eventWithPath.context();
            final Path child = dir.resolve(name);
            if (Files.isDirectory(child)) {
                addPathToWatcher(child);
            }

        }
    }

    @Override
    public void close() throws Exception {
        addSubscriber.getAndSet((it) -> {
        });
        task.cancel(false);
        watcher.close();
        directoriesByKey.clear();
        subscriberQueue.clear();
    }


}
