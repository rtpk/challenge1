package com.technical.node;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class NodeIterable<T> implements Iterable {
    private Node<T> node;

    public NodeIterable(Node<T> node) {
        this.node = node;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {

            ArrayDeque<Node<T>> knownBranchesToTravese = new ArrayDeque<>();
            ArrayDeque<Node<T>> nodesToReturn = new ArrayDeque<>();

            {
                Collections.addAll(nodesToReturn, NodeIterable.this.node.getArray());
                nodesToReturn.stream().filter(node -> node.getArray().length > 0).forEach(knownBranchesToTravese::push);
            }

            @Override
            public boolean hasNext() {
                return (!knownBranchesToTravese.isEmpty() || !nodesToReturn.isEmpty());
            }

            @Override
            public T next() {
                if (knownBranchesToTravese.isEmpty() && nodesToReturn.isEmpty()) throw new NoSuchElementException();

                if (!nodesToReturn.isEmpty()) {
                    return nodesToReturn.poll().getPayload();
                }

                Node<T> resultNode = knownBranchesToTravese.pop();

                if (resultNode.getArray().length > 0) {
                    Collections.addAll(nodesToReturn, resultNode.getArray());
                }

                if (!nodesToReturn.isEmpty()) {
                    nodesToReturn.stream().filter(node -> node.getArray().length > 0).forEach(knownBranchesToTravese::push);
                }
                return resultNode.getPayload();
            }
        };
    }

}
