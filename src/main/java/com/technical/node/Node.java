package com.technical.node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;


public interface Node<T extends NodeWrapper> extends Iterable<Node> {
    NodeWrapper getNode();

    default Iterator<Node> iterator() {
        return new Iterator<Node>() {
            Stack<Map.Entry<Node, Integer>> branchStack = new Stack<>();

            int childPosition = 0;
            Node currentItem = Node.this;

            @Override
            public boolean hasNext() {
                return !(branchStack.isEmpty() && getNodeSize() <= childPosition);
            }

            @Override
            public Node next() {

                if (childPosition >= getNodeSize()) {
                    Map.Entry<Node, Integer> entry = branchStack.pop();
                    childPosition = entry.getValue();
                    currentItem = entry.getKey();
                    return next();
                }
                if (getChild(childPosition).getNode().isBranch()) {
                    if(childPosition+1<getNodeSize())
                        branchStack.push(new HashMap.SimpleEntry<>(currentItem, childPosition + 1));
                    currentItem = getChild(childPosition);
                    childPosition = 0;
                    return currentItem;
                }
                if (childPosition < getNodeSize()) {
                    Node temp = getChild(childPosition++);
                    return temp;
                }
                return null;
            }

            NodeWrapper getNode() {
                return currentItem.getNode();
            }

            Node getChild(int index) {
                return getNode().getChild(index);
            }

            int getNodeSize() {
                return getNode().getSize();
            }
        };
    }
}
