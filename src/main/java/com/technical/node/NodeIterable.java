package com.technical.node;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class NodeIterable<T> implements Iterable<T> {
    private Node<T> node;

    public NodeIterable(Node<T> node) {
        this.node = node;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {

            ArrayDeque<Node<T>> nodesToReturn = new ArrayDeque<>();

            {
                Collections.addAll(nodesToReturn, NodeIterable.this.node.getArray());
            }

            @Override
            public boolean hasNext() {
                return (!nodesToReturn.isEmpty());
            }

            @Override
            public T next() {
                if (nodesToReturn.isEmpty()) throw new NoSuchElementException();

                Node<T> node = nodesToReturn.poll();
                if (node.getArray().length > 0) {
                    Collections.addAll(nodesToReturn, node.getArray());
                }

                return node.getPayload();
            }
        };
    }

}
