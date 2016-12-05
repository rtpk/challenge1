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

    public static Observable<WatchEvent<?>> watch(final Path path) throws IOException, InterruptedException {
        return new ObservableFactory(path).create();
    }

    private static class ObservableFactory implements AutoCloseable {

        private final WatchService watcher;
        private final Path directory;
        private final ConcurrentMap<WatchKey, Path> directoriesByKey = new ConcurrentHashMap<>();
        private final Queue<Subscriber> subscriberList = new ConcurrentLinkedQueue<>();
        private final ExecutorService executor = Executors.newSingleThreadExecutor();
        private boolean errorFree = true;
        private Future task;

        private ObservableFactory(final Path path) throws IOException, InterruptedException {
            System.out.println("konstruktor");
            final FileSystem fileSystem = path.getFileSystem();
            watcher = fileSystem.newWatchService();
            directory = path;
            startAddAllToWatcher();

          //  task = executor.submit(this::watchFolders);//nie dziala do konca prawidlowo
        }

        private void watchFolders() {
            System.out.println("POCZATEK WATKU");
            while (errorFree) {
                final WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException exception) {
                    subscriberList.forEach(s -> s.onError(exception)); //tutaj nie tak jak powinno..
                    errorFree = false;
                    break;
                }
                final Path dir = directoriesByKey.getOrDefault(key, directory);
                for (final WatchEvent<?> event : key.pollEvents()) {
                    subscriberList.forEach(s -> s.onNext(event));
                    addNewPathToWatcher(dir, event);
                }
                boolean valid = key.reset();
                if (!valid) { //co bedzie jak valid i brake
                    directoriesByKey.remove(key);
                    if (directoriesByKey.isEmpty()) {
                        break;
                    }
                }
            }  //poprawic kontrakt o usuwaniu plikow i ich co dzieje sie z ich rejestracja wtedy -> dodac do kontraktu -> test
            if (errorFree) {
                subscriberList.forEach(Observer::onCompleted);
            }
            System.out.println("KONIEC WATKU");
        }

        private Observable<WatchEvent<?>> create() {
            return Observable.create(subscriber -> {
                subscriberList.add(subscriber);
            });
        }

        private void startAddAllToWatcher() throws InterruptedException {
            try {
                addAllPathsByIteratorToWatcher(directory);
            } catch (IOException exception) {
                subscriberList.forEach(s -> s.onError(exception));
                errorFree = false;
            }
        }

        private void addAllPathsByIteratorToWatcher(final Path rootDirectory) throws IOException, InterruptedException {
            NodeIterable<Path> root = new NodeIterable<>(new NodePath(rootDirectory));
            NodeIterableRx temp = new NodeIterableRx();
            Observable<Path> result = temp.convert(root);
            result.filter(element -> Files.isDirectory(element)).forEach((dir) -> {
                try {
                    addPathToWatcher(dir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        private void addPathToWatcher(final Path dir) throws IOException {
            System.out.println("dziala");
            final WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            directoriesByKey.putIfAbsent(key, dir);
        }

        private void addNewPathToWatcher(final Path dir, final WatchEvent<?> event) {
            final Kind<?> kind = event.kind();
            if (kind.equals(ENTRY_CREATE)) {
                final WatchEvent<Path> eventWithPath = cast(event);
                final Path name = eventWithPath.context();
                final Path child = dir.resolve(name);
                try {
                    if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                        addPathToWatcher(child);
                    }
                } catch (final IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        @SuppressWarnings("unchecked")
        static <T> WatchEvent<T> cast(WatchEvent<?> event) {
            return (WatchEvent<T>) event;
        }

        @Override
        public void close() throws Exception {
            task.cancel(true);
        }
    }
}