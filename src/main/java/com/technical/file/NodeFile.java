package com.technical.file;

import com.technical.node.Node;

import java.io.File;
import java.util.Arrays;

public class NodeFile implements Node {

    private java.io.File file;

    public NodeFile(java.io.File file) {
        this.file = file;
    }

    @Override
    public Node[] getArray() {
        File[] result = file.listFiles();
        if (result == null)
            return new NodeFile[0];
        else
            return Arrays.stream(result).map(p -> (new NodeFile(p))).toArray(NodeFile[]::new);
    }

    @Override
    public String getPayload() {
        return file.getAbsolutePath();
    }

}
