package com.technical.node;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class NodeIterable<T extends Node> implements Iterable {
    private T node;

    public NodeIterable(T node) {
        this.node = node;
    }

    private T getNode() {
        return node;
    }


    public Iterator<T> iterator() {
        return new Iterator<T>() {

            ArrayDeque<ArrayDeque<Node>> branchesStack = new ArrayDeque<>();
            ArrayDeque<Node> nodesQueue = new ArrayDeque<>();
            @SuppressWarnings("unchecked")
            T node = NodeIterable.this.getNode();
            {
                Collections.addAll(nodesQueue, node.getArray());
            }

            @Override
            public boolean hasNext() {
                return (!branchesStack.isEmpty() || !nodesQueue.isEmpty());
            }

            @Override
            public T next() {

                if (branchesStack.isEmpty() && nodesQueue.isEmpty()) throw new NoSuchElementException();

                if (nodesQueue.isEmpty()) {
                    nodesQueue.addAll(branchesStack.pop());
                }

                node = (T) nodesQueue.poll();
                if (node.getArray().length > 0) {
                    if (!nodesQueue.isEmpty())
                        branchesStack.push(nodesQueue.clone());
                    nodesQueue.clear();
                    Collections.addAll(nodesQueue, node.getArray());
                }
                return node;

            }
        };
    }

}
