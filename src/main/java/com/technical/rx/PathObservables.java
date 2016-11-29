package com.technical.rx;

import com.technical.file.NodePath;
import com.technical.node.NodeIterable;
import rx.Observable;
import rx.Subscriber;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public final class PathObservables {

    private PathObservables() {
    }

    public static Observable<WatchEvent<?>> watchRecursive(final Path path) throws IOException {
        return new ObservableFactory(path).create();
    }


    private static class ObservableFactory {

        private final WatchService watcher;
        private final Map<WatchKey, Path> directoriesByKey = new HashMap<>();
        private final Path directory;

        private ObservableFactory(final Path path) throws IOException {
            final FileSystem fileSystem = path.getFileSystem();
            watcher = fileSystem.newWatchService();
            directory = path;
        }

        private Observable<WatchEvent<?>> create() {
            return Observable.create(subscriber -> {
                boolean errorFree = true;
                try {
                    registerAll(directory);

                } catch (IOException exception) {
                    subscriber.onError(exception);
                    errorFree = false;
                }
                while (errorFree) {
                    final WatchKey key;
                    try {
                        key = watcher.take();
                    } catch (InterruptedException exception) {
                        subscriber.onError(exception);
                        errorFree = false;
                        break;
                    }
                    final Path dir = directoriesByKey.get(key);
                    for (final WatchEvent<?> event : key.pollEvents()) {
                        subscriber.onNext(event);
                        registerNewDirectory(subscriber, dir, event);
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
                    subscriber.onCompleted();
                }
            });
        }

        //work in progress
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
            directoriesByKey.put(key, dir);
        }

        private void registerNewDirectory(final Subscriber<? super WatchEvent<?>> subscriber, final Path dir,  final WatchEvent<?> event) {
            final Kind<?> kind = event.kind();
            if (kind.equals(ENTRY_CREATE)) {
                @SuppressWarnings("unchecked")
                final WatchEvent<Path> eventWithPath = (WatchEvent<Path>) event;
                final Path name = eventWithPath.context();
                final Path child = dir.resolve(name);
                try {
                    if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                        register(child);
                    }
                } catch (final IOException exception) {
                    subscriber.onError(exception);
                }
            }
        }
    }
}