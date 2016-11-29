package com.technical;

import com.technical.rx.FileRx;
import com.technical.rx.PathObservables;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ReactiveTests {

    private void print(File obj) {
        log.info("Got: {}", obj.getAbsoluteFile());
    }

    @Test
    public void shouldObserveAddOneFile() throws IOException, InterruptedException {

        //Given
        FileSystem rootFile = FileSystems.getDefault(); //Jimfs.newFileSystem(Configuration.unix());
        FileRx fileRx = new FileRx(rootFile.getPath("C:\\Users\\rtpk\\Downloads\\New folder"));
        Observable<FileRx> fileRxObservable = Observable.just(fileRx);
        List<Path> listResult = Collections.synchronizedList(new ArrayList<>());

        Observable<Path> fileObservable = fileRx.observeChanges();

        fileRxObservable.observeOn(Schedulers.io()).subscribe(p -> {
            System.out.println("TEST " + p.getKeys()); //work in progress
            p.processEvents();

        });

        //When
        Path file = rootFile.getPath("C:\\Users\\rtpk\\Downloads\\New folder\\New folder\\file.txt");
        Files.createFile(file);
        Thread.sleep(1000);

        //Then
        assertThat(listResult).contains(file);
    }

    @Test
    public void shouldObserveAddOneFileObservable() throws IOException, InterruptedException {

        //Given
        FileSystem rootFile = FileSystems.getDefault(); //Jimfs.newFileSystem(Configuration.unix());
        final Observable<WatchEvent<?>> observable = PathObservables.watchRecursive(rootFile.getPath("C:\\Users\\rtpk\\Downloads\\New folder"));
        final List<WatchEvent<?>> events = new LinkedList<>();

        observable.subscribeOn(Schedulers.io()).subscribe(events::add);
        Thread.sleep(1000);
        //When
        Path file = rootFile.getPath("C:\\Users\\rtpk\\Downloads\\New folder\\New folder\\file.txt");
        Files.createFile(file);
        Thread.sleep(1000);

        //Then
        final WatchEvent<?> event = events.get(0);
        assertThat(event.context()).isEqualTo(file.getFileName());
        Files.delete(file);
    }


}
