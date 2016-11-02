package com.technical.node;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class NodeIterable<T extends Node> implements Iterable {  //wykazac po co <T extends>
    private T node;

    public NodeIterable(T node) {
        this.node = node;
    }

    private T getNode() {
        return node;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {

            ArrayDeque<ArrayDeque<Node>> branchStack = new ArrayDeque<>();
            ArrayDeque<Node> stack = new ArrayDeque<>();
            T node = NodeIterable.this.getNode();

            {
                Collections.addAll(stack, node.getArray());
            }

            @Override
            public boolean hasNext() {
                return (!branchStack.isEmpty() || !stack.isEmpty());
            }

            @Override
            public T next() {

                if (!(!branchStack.isEmpty() || !stack.isEmpty())) throw new NoSuchElementException();

                if (stack.isEmpty()) {
                    stack.addAll(branchStack.pop());
                }

                node = (T) stack.poll();
                if (node.getArray().length > 0) {
                    if (!stack.isEmpty())
                        branchStack.push(stack.clone());
                    stack.clear();
                    Collections.addAll(stack, node.getArray());
                }
                return node;

            }
        };
    }

}
