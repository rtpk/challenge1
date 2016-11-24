package com.technical;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.technical.file.NodePath;
import com.technical.node.NodeIterable;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FileTestsJimfs {

    @Test
    public void hasNextOneElementCollection() throws IOException {
        //Given
        FileSystem rootFile = Jimfs.newFileSystem(Configuration.unix());
        Path rootPath = rootFile.getPath("/root");
        Path folder = Files.createDirectories(rootPath);
        Path file = rootFile.getPath("/root/files3.txt");
        Files.createFile(file);

        //When
        NodeIterable<Path> root = new NodeIterable<>(new NodePath(folder));
        Iterator<Path> it = root.iterator();

        //Then
        assertThat(it.hasNext()).isTrue();
    }

    @Test
    public void testIteratorOnFiles() throws IOException {
        //Given
        FileSystem rootFile = Jimfs.newFileSystem(Configuration.unix());

        Files.createDirectories(rootFile.getPath("/root/folder1"));
        Files.createDirectories(rootFile.getPath("/root/folder3/folder4"));
        Files.createDirectories(rootFile.getPath("/root/folder2/folder5"));

        Files.createFile(rootFile.getPath("/root/folder1/file2.txt"));
        Files.createFile(rootFile.getPath("/root/folder2/folder5/file1.txt"));
        Files.createFile(rootFile.getPath("/root/file2.txt"));
        Files.createFile(rootFile.getPath("/root/file3.txt"));

        //When
        NodeIterable<Path> root = new NodeIterable<>(new NodePath(rootFile.getPath("/root")));

        Iterator<Path> it = root.iterator();
        List<String> paths = new ArrayList<>();
        while (it.hasNext()) {
            paths.add(it.next().toString());
        }

        //Then
        assertThat(paths).contains(
                "/root/file2.txt",
                "/root/file3.txt",
                "/root/folder1",
                "/root/folder2",
                "/root/folder3",
                "/root/folder3/folder4",
                "/root/folder2/folder5",
                "/root/folder2/folder5/file1.txt",
                "/root/folder1/file2.txt"
        );
    }

    @Test
    public void testMoreThanOneIteratorOnFiles() throws IOException {
        //Given
        FileSystem rootFile = Jimfs.newFileSystem(Configuration.unix());
        Files.createDirectories(rootFile.getPath("/root/folder2/folder5"));
        Files.createFile(rootFile.getPath("/root/folder2/file2.txt"));

        //When
        NodeIterable<Path> root = new NodeIterable<>(new NodePath(rootFile.getPath("/root")));

        Iterator firstIterator = root.iterator();
        Iterator secondIterator = root.iterator();

        //Then
        assertThat((firstIterator. next())).isEqualTo((secondIterator.next()));
    }


}
