package com.technical.node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class NodeIterable<T extends Node> implements Iterable {  ///wykazaac po co
    private T node;

    public NodeIterable(T node) {
        this.node = node;
    }

    private T getNode() {
        return node;
    }

 //
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Stack<Map.Entry<T, Integer>> branchStack = new Stack<>();

            int childPosition = 0; //lepsza nazwa
            T currentBranch = NodeIterable.this.getNode();  //byla czy jest zwrocona -> byl - czy bedzie ?

            @Override
            public boolean hasNext() {
                return !(branchStack.isEmpty() && currentBranch.getSize() <= childPosition);
            }

            @Override
            public T next() {
                //trzymac getArray np. w kolejce

                //konktrat noSucheElementException --- zamiast null

                if (branchStack.isEmpty() && currentBranch.getSize() <= childPosition) return null;

                if (childPosition >= currentBranch.getSize()) { //pozabyc sie getSize  //currentBranch nazwa lokalna w metodzie
                    Map.Entry<T, Integer> entry = branchStack.pop();
                    childPosition = entry.getValue(); //mialem bede mial?
                    currentBranch = entry.getKey();   // stacku - iterable od node?
                    return next();
                }

                if (currentBranch.getArray()[childPosition].isBranch()) {
                    if (childPosition + 1 < currentBranch.getSize()) {
                        branchStack.push(new HashMap.SimpleEntry<>(currentBranch, childPosition + 1));
                    }
                    currentBranch = (T) currentBranch.getArray()[childPosition]; //sprawdzic i w razie koniecznosci przerobic - uzasadnic/uproscic
                    childPosition = 0;
                    return currentBranch; //nie ma czegos takiego jak currentBranch  - operowac na samym stosie (bez child position - iterable )
                }

                if (childPosition < currentBranch.getSize()) {
                    int index = childPosition++;
                    return (T) currentBranch.getArray()[index];
                }
                return null;
            }
        };
    }

}
