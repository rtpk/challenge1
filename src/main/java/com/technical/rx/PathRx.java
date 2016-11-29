package com.technical.rx;

import com.technical.file.NodePath;
import com.technical.node.NodeIterable;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public final class PathRx implements Runnable {

    private Thread t;
    private final WatchService watcher;
    private final Map<WatchKey, Path> directoriesByKey = new HashMap<>();
    private final Path directory;
    private final List<Subscriber> subscriberList = new ArrayList<>();

    public PathRx(final Path path) throws IOException {
        final FileSystem fileSystem = path.getFileSystem();
        watcher = fileSystem.newWatchService();
        directory = path;
    }

    public void start() {
        System.out.println("Starting ");
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    public void run() {
        System.out.println("My thread is in running state");

        boolean errorFree = true;
        try {
            registerAll(directory);
        } catch (IOException exception) {
            subscriberList.forEach(s -> s.onError(exception));
            errorFree = false;
        }
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
    }

    public Observable<WatchEvent<?>> watch() {
        //lokalna lista
        //drugi watek blokujace zdarzenie - jak cos sie zjawi wysle do wszystkich na liscie
        //na koncu czyszczecnie zasobow wlasnych dla watku
        //wyjątek IterruptedException
        return Observable.create(subscriberList::add);
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
            @SuppressWarnings("unchecked")
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