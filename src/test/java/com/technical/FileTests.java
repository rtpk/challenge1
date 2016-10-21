package com.technical;

public class FileTests {
//
//    @Rule
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
//
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
//        assertThat(paths).contains(
//                folder.getRoot().toString() + "/folder1/file2.txt",
//                folder.getRoot().toString() + "/folder3/folder4",
//                folder.getRoot().toString() + "/folder2/folder5/file1.txt",
//                folder.getRoot().toString() + "/file3.txt");
//    }

//    @Test
//    public void testIteratorExists() {
//        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder\\test\\dsa\\zxc\\aa");
//        Node root = new NodeImpl(new FileWrapper(file));
//        for (Object node : root) {
//            System.out.println(node.toString());
//        }
//    }

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
//    }

}
