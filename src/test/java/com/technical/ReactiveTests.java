package com.technical;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.technical.rx.PathRx;
import org.junit.Test;
import rx.Observable;
import rx.subjects.ReplaySubject;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.WatchEvent;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactiveTests {

    @Test
    public void shouldObserveAddOneFile() throws IOException, InterruptedException {
        //Given
        FileSystem rootFile = Jimfs.newFileSystem(Configuration.unix());
        Files.createDirectories(rootFile.getPath("/root/folder1"));
        final Observable<WatchEvent<?>> observable = PathRx.watch(rootFile.getPath("/root"));

        ReplaySubject<WatchEvent<?>> replaySubject = ReplaySubject.create();
        observable.subscribe(replaySubject);

        //When
        Files.createFile(rootFile.getPath("/root/folder1/file2.txt"));

        //Then
        final WatchEvent<?> event = replaySubject.toBlocking().first();
        assertThat(event.context()).isEqualTo(rootFile.getPath("/root/folder1/file2.txt").getFileName());
    }

}
