package com.technical.node;

/**
 * Created by Robert Piotrowski on 04/10/2016.
 */
public class NodeImpl implements Node<NodeWrapper> {

    private NodeWrapper node;

    public NodeImpl(NodeWrapper node) {
        this.node = node;
    }

    public NodeWrapper getNode() {
        return node;
    }

    public String toString() {
        return node.toString();
    }


}
