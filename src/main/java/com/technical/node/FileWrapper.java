package com.technical.node;

import java.io.File;

public class FileWrapper implements NodeWrapper {

    private File file;
    private File[] fileList;

    public FileWrapper(File file) {
        this.file = file;
        this.fileList = file.listFiles();
    }

    @Override
    public Node getChild(int index) {
        if (file.isFile()) return new Node(new FileWrapper(file));
        return new Node(new FileWrapper(fileList[index]));
    }

    @Override
    public int getSize() {
        if (file.isFile()) return 1;
        return fileList.length;
    }

    @Override
    public boolean isBranch() {
        return file.isDirectory();
    }

    @Override
    public String toString() {
        return file.getAbsolutePath();
    }

}
