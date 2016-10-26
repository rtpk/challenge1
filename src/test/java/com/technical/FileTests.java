package com.technical;

import com.technical.node.Node;
import com.technical.file.NodeFile;
import com.technical.node.NodeIterable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FileTests {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void hasNextEmptyCollection() {
        NodeIterable<NodeFile> root = new NodeIterable<>(new NodeFile(folder.getRoot()));
        Iterator it = root.iterator();
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    public void hasNextOneElementCollection() throws IOException {
        folder.newFile("file3.txt");
        NodeIterable<NodeFile> root = new NodeIterable<>(new NodeFile(folder.getRoot()));
        Iterator it = root.iterator();
        assertThat(it.hasNext()).isTrue();
    }

    @Test
    public void testIteratorOnFiles() throws IOException {

        folder.newFolder("folder1", "file2.txt");
        folder.newFolder("folder3", "folder4");
        folder.newFolder("folder2", "folder5", "file1.txt");
        folder.newFile("file3.txt");

        NodeIterable<NodeFile> root = new NodeIterable<>(new NodeFile(folder.getRoot()));

        Iterator it = root.iterator();
        List<String> paths = new ArrayList<>();

        while (it.hasNext()) {
            paths.add(((Node)it.next()).getPayload());
        }
        assertThat(paths).contains(
                folder.getRoot().toString() + "\\folder1\\file2.txt",
                folder.getRoot().toString() + "\\folder3\\folder4",
                folder.getRoot().toString() + "\\folder2\\folder5\\file1.txt",
                folder.getRoot().toString() + "\\file3.txt");
    }

    @Test
    public void testMoreThanOneIteratorOnFiles() throws IOException {

        folder.newFolder("folder1", "file2.txt");
        folder.newFolder("folder3", "folder4");

        NodeIterable<NodeFile> root = new NodeIterable<>(new NodeFile(folder.getRoot()));

        Iterator firstIterator = root.iterator();
        Iterator secondIterator = root.iterator();

        assertThat(((Node)firstIterator.next()).getPayload()).isEqualTo(((Node)secondIterator.next()).getPayload());
    }


//    @Test
//    public void testIteratorExists() {
//        File file = new File("C:\\Users\\rtpk\\Downloads");
//        NodeIterable<NodeFile> root = new NodeIterable<>(new NodeFile(file));
//
//        for (Object node : root) {
//            System.out.println(node.toString());
//        }
//    }
}
