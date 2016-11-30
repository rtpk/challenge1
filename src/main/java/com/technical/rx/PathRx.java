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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

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
        private final Map<WatchKey, Path> directoriesByKey = new HashMap<>();
        private final Path directory;
        private final BlockingQueue<Subscriber> subscriberList = new LinkedBlockingQueue<>();
        boolean errorFree = true;

        private ObservableFactory(final Path path) throws IOException {
            final FileSystem fileSystem = path.getFileSystem();
            watcher = fileSystem.newWatchService();
            directory = path;
            takeWait();
        }

        public void takeWait() {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                System.out.println("nowy watek");
                try {
                    registerAll(directory);
                } catch (IOException exception) {
                    subscriberList.forEach(s -> s.onError(exception));
                    errorFree = false;
                }
                while (errorFree) {
                    System.out.println("subscriberList size " + subscriberList.size());
                    final WatchKey key;
                    key = watcher.poll();
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
                    subscriberList.forEach(p -> p.onCompleted());
                }
            });
        }

        private Observable<WatchEvent<?>> create() {
            return Observable.create(subscriber -> {
                subscriberList.add(subscriber);
                System.out.println(subscriber.toString());

                //lokalna lista
                //drugi watek blokujace zdarzenie - jak cos sie zjawi wysle do wszystkich na liscie
                //na koncu czyszczecnie zasobow wlasnych dla watku
                //wyjątek IterruptedException

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