package com.technical;

import com.technical.node.FileWrapper;
import com.technical.node.Node;
import com.technical.node.NodeImpl;
import org.junit.Test;

import java.io.File;

public class NodeImplTests {
//
//    @Rule  //do testow integracyjnych
//    public TemporaryFolder folder = new TemporaryFolder();
//
//    @Test
//    public void hasNextEmptyCollection() {
//        Node root = new NodeImpl(new FileWrapper(folder.getRoot()));
//        Iterator it = root.iterator();
//        assertThat(it.hasNext()).isFalse();
//    }
//
//    @Test
//    public void hasNextOneElementCollection() throws IOException {
//        folder.newFile("file3.txt");
//        Node root = new NodeImpl(new FileWrapper(folder.getRoot()));
//        Iterator it = root.iterator();
//        assertThat(it.hasNext()).isTrue();
//    }

//    @Test
//    public void testIteratorOnFiles() throws IOException {
//
//        folder.newFolder("folder1", "file2.txt");
//        folder.newFolder("folder3", "folder4");
//        folder.newFolder("folder2", "folder5", "file1.txt");
//        folder.newFile("file3.txt");
//
//        Node root = new NodeImpl(new FileWrapper(folder.getRoot()));
//
//        Iterator it = root.iterator();
//        List<String> paths = new ArrayList<>();
//
//        while (it.hasNext()) {
//            paths.add(it.next().toString());
//        }
//
//        //testy na abstrakcjach - nie na plikach / bez tworzenia
//        assertThat(paths).contains(
//                folder.getRoot().toString() + "/folder1/file2.txt",
//                folder.getRoot().toString() + "/folder3/folder4",
//                folder.getRoot().toString() + "/folder2/folder5/file1.txt",
//                folder.getRoot().toString() + "/file3.txt");
//
//        //test w oprac
//    }

    @Test
    public void testIteratorExists() {
        File file = new File("C:\\Users\\rtpk\\Downloads");
        Node root = new NodeImpl(new FileWrapper(file));
        for (Object node : root) {
            System.out.println(node.toString());
        }
    }

//
//    @Test
//    public void shouldIterateWithoutHasNext() throws IOException {
//
//        folder.newFile("jeden.txt");
//        Node root = new NodeImpl(new FileWrapper(folder.getRoot()));
//
//        String file = root.iterator().next().toString();
//        assertThat(file).isEqualTo(folder.getRoot().toString() + "\\jeden.txt");
//
//    }
//
//
//    @Test
//    public void testMoreThanOneIteratorOnFiles() throws IOException {
//
//        folder.newFolder("folder1", "file2.txt");
//        folder.newFolder("folder3", "folder4");
//
//        Node root = new NodeImpl(new FileWrapper(folder.getRoot()));
//
//        Iterator firstIterator = root.iterator();
//        Iterator secondIterator = root.iterator();
//
//        assertThat(firstIterator.next().toString()).isEqualTo(secondIterator.next().toString());
//
//    }

}
