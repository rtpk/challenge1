package com.technical;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.technical.rx.PathRx;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import rx.Observable;
import rx.subjects.ReplaySubject;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.WatchEvent;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ReactiveTests {

    private void print(File obj) {
        log.info("Got: {}", obj.getAbsoluteFile());
    }

    @Test
    public void shouldObserveAddOneFile() throws IOException, InterruptedException {
        //Given
        FileSystem rootFile = Jimfs.newFileSystem(Configuration.unix());
        final Observable<WatchEvent<?>> observable = PathRx.watch(rootFile.getPath("/root"));
        Files.createDirectories(rootFile.getPath("/root/folder1"));

        ReplaySubject<WatchEvent<?>> replaySubject = ReplaySubject.create();
        ReplaySubject<WatchEvent<?>> replaySubject2 = ReplaySubject.create();
        observable.subscribe(replaySubject);
        observable.subscribe(replaySubject2);

        //When
        Files.createFile(rootFile.getPath("/root/folder1/file2.txt"));
        Files.createDirectories(rootFile.getPath("/root/folder1/file32/file4"));

        //Then
        final WatchEvent<?> event = replaySubject.toBlocking().first();
        replaySubject2.subscribe(p -> System.out.println("ReactiveTests.shouldObserveAddOneFile"));
        assertThat(event.context()).isEqualTo(rootFile.getPath("/root/folder1/file2.txt").getFileName());
    }


}
