package com.technical.node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class NodeIterable<T extends Node> implements Iterable {
    private T node;

    public NodeIterable(T node) {
        this.node = node;
    }

    private T getNode() {
        return node;
    }

    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Stack<Map.Entry<T, Integer>> branchStack = new Stack<>();

            int childPosition = 0;
            T currentItem = NodeIterable.this.getNode();  //nextItem

            @Override
            public boolean hasNext() {
                return !(branchStack.isEmpty() && currentItem.getSize() <= childPosition);
            }

            @Override
            public T next() {

                if (branchStack.isEmpty() && currentItem.getSize() <= childPosition) return null;

                if (childPosition >= currentItem.getSize()) {
                    Map.Entry<T, Integer> entry = branchStack.pop();
                    childPosition = entry.getValue();
                    currentItem = entry.getKey();
                    return next();
                }
                if (currentItem.getArray()[childPosition].isBranch()) {
                    if (childPosition + 1 < currentItem.getSize()) {
                        branchStack.push(new HashMap.SimpleEntry<>(currentItem, childPosition + 1));
                    }
                    currentItem = (T) currentItem.getArray()[childPosition];
                    childPosition = 0;
                    return currentItem;
                }
                if (childPosition < currentItem.getSize()) {
                    int index = childPosition++;
                    return (T) currentItem.getArray()[index];
                }
                return null;
            }
        };
    }

}
