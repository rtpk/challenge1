package com.technical.file;

import com.technical.node.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class NodePath implements Node<Path> {

    private Path file;

    public NodePath(Path file) {
        this.file = file;
    }

    @Override
    public Node[] getArray() {

        if (Files.isDirectory(file)) {
            try {
                return Files.list(file).map(p -> (new NodePath(p))).toArray(NodePath[]::new);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new NodePath[0];
    }

    @Override
    public Path getPayload() {
        return file;
    }

}