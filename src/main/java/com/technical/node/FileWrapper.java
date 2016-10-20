package com.technical.node;

import java.io.File;

/**
 * Created by Robert Piotrowski on 19/10/2016.
 */
public class FileWrapper implements NodeWrapper {

    private File file;

    public FileWrapper(File file) {
        this.file = file;
    }

    @Override
    public Node getChild(int index) {
        if (file.isFile()) return new NodeImpl(new FileWrapper(file));
        return new NodeImpl(new FileWrapper(file.listFiles()[index]));
    }

    @Override
    public int getSize() {
        if (file.isFile()) return 1;
        return file.list().length;
    }

    @Override
    public boolean isContrainer() {
        return file.isDirectory();
    }

    @Override
    public String toString() {
        return file.getAbsolutePath();
    }

}
