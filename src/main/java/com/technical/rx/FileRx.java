package com.technical.rx;

import com.technical.file.NodePath;
import com.technical.node.NodeIterable;
import rx.Observable;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class FileRx {

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final Path rootFile;

    //Creates a WatchService and registers the given directory
    public FileRx(Path dir) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();
        this.rootFile = dir;
        System.out.println("Done");
    }

    //Allows observe changes like adding files and folders in root
    public Observable<Path> observeChanges() {
        NodeIterable<Path> root = new NodeIterable<>(new NodePath(rootFile));
        NodeIterableRx<Path> temp = new NodeIterableRx<>();
        Observable<Path> result = temp.convert(root);
        result.filter(element -> Files.isDirectory(element)).forEach(this::register);
        return result;
    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    //Process all events for keys queued to the watcher
    public void processEvents() {
        for (; ; ) {

            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = getKeys().get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
                if (kind == OVERFLOW) {
                    continue;
                }

                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                System.out.format("%s: %s\n", event.kind().name(), child);

                if (Files.isDirectory(child))
                    register(child);
            }

            boolean valid = key.reset();
            if (!valid) {
                getKeys().remove(key);

                if (getKeys().isEmpty()) {
                    break;
                }
            }
        }
    }

    private void register(Path dir) {
        System.out.println(" register dir = " + dir);
        WatchKey key = null;
        try {
            key = dir.register(watcher, ENTRY_CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getKeys().put(key, dir);
    }

    public Map<WatchKey, Path> getKeys() {
        return keys;
    }
}

