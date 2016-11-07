package com.technical.rx;

import com.technical.node.Node;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

class NodeIterableRx<T extends Node> implements Iterable {
    private T node;

    public NodeIterableRx(T node) {
        this.node = node;
    }

    private T getNode() {
        return node;
    }

    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            ArrayDeque<ArrayDeque<Node>> branchStack = new ArrayDeque<>();
            ArrayDeque<Node> queue = new ArrayDeque<>();
            T node = NodeIterableRx.this.getNode();
            {
                Collections.addAll(queue, node.getArray());
            }

            @Override
            public boolean hasNext() {
                return (!branchStack.isEmpty() || !queue.isEmpty());
            }

            @Override
            public T next() {

                if (branchStack.isEmpty() && queue.isEmpty()) throw new NoSuchElementException();

                if (queue.isEmpty()) {
                    queue.addAll(branchStack.pop());
                }

                node = (T) queue.poll();
                if (node.getArray().length > 0) {
                    if (!queue.isEmpty())
                        branchStack.push(queue.clone());
                    queue.clear();
                    Collections.addAll(queue, node.getArray());
                }
                return node;

            }
        };
    }

}
