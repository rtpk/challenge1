package com.technical.tests;

import com.technical.node.Node;

import java.util.Arrays;

public class NodeTestImpl implements Node {

    private TestObject parent;

    public NodeTestImpl(TestObject parent) {
        this.parent = parent;
    }

    @Override
    public Node[] getArray() {
            return Arrays.stream(parent.getList().toArray()).map(p -> (new NodeTestImpl((TestObject) p))).toArray(NodeTestImpl[]::new);
    }

    @Override
    public int getSize() {
        if (!isBranch()) return 0;
        return getArray().length;
    }

    @Override
    public boolean isBranch() {
        return (parent.getList() != null);
    }

    @Override
    public String getPayload() {
        return String.valueOf(parent.getName());
    }

}
