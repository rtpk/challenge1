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
        if (file.listFiles() == null)
            return new NodeFile[0];
        else
            return Arrays.stream(file.listFiles()).map(p -> (new NodeFile(p))).toArray(NodeFile[]::new);
    }

    @Override
    public String getPayload() {
        return file.getAbsolutePath();
    }

}
