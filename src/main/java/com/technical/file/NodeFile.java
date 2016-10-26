package com.technical.file;

import com.technical.node.Node;

import java.util.Arrays;

public class NodeFile implements Node {

    private java.io.File file;

    public NodeFile(java.io.File file) {
        this.file = file;
    }

    @Override
    public Node[] getArray() {
        return Arrays.stream(file.listFiles()).map(p -> (new NodeFile(p))).toArray(NodeFile[]::new);
    }

    @Override
    public int getSize() {
        if (file.isFile()) return 1;
        return getArray().length;
    }

    @Override
    public boolean isBranch() {
        return file.isDirectory();
    }

    @Override
    public String getPayload() {
        return file.getAbsolutePath();
    }

}
