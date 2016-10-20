package com.technical;

import com.technical.node.FileWrapper;
import com.technical.node.Node;
import com.technical.node.NodeImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;


/**
 * Created by Robert Piotrowski on 04/10/2016.
 */
public class NodeImplTests {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void hasNextEmptyCollection() {
        Node root = new NodeImpl(new FileWrapper(folder.getRoot()));
        Iterator it = root.iterator();
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    public void hasNextOneElementCollection() throws IOException {
        folder.newFile("file3.txt");
        Node root = new NodeImpl(new FileWrapper(folder.getRoot()));
        Iterator it = root.iterator();
        assertThat(it.hasNext()).isTrue();
    }

    @Test
    public void testIteratorOnFiles() throws IOException {

        folder.newFolder("folder1", "file2.txt");
        folder.newFolder("folder3", "folder4");
        folder.newFolder("folder2", "folder5", "file1.txt");
        folder.newFile("file3.txt");

        Node root = new NodeImpl(new FileWrapper(folder.getRoot()));

        Iterator it = root.iterator();
        List<String> paths = new ArrayList<>();

        while (it.hasNext()) {
            paths.add(it.next().toString());
        }

        assertThat(paths).contains(folder.getRoot().toString() + "\\file3.txt",
                folder.getRoot().toString() + "\\folder3\\folder4",
                folder.getRoot().toString() + "\\folder1\\file2.txt",
                folder.getRoot().toString() + "\\folder2\\folder5\\file1.txt");
    }


    @Test
    public void testMoreThanOneIteratorOnFiles() throws IOException {

        folder.newFolder("folder1", "plik2.txt");
        folder.newFolder("folder3", "folder4");

        Node root = new NodeImpl(new FileWrapper(folder.getRoot()));

        Iterator firstIterator = root.iterator();
        Iterator secondIterator = root.iterator();

        assertThat(firstIterator.next().toString()).isEqualTo(secondIterator.next().toString());

    }

}
