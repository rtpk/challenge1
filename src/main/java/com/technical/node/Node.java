package com.technical.node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Robert Piotrowski on 04/10/2016.
 */
public interface Node<T extends NodeWrapper> extends Iterable<Node> {
    NodeWrapper getNode();

    default Iterator<Node> iterator() {

        Iterator<Node> it = new Iterator<Node>() {
            Stack<Map.Entry<Node, Integer>> dirStack = new Stack<>();

            int index = 0;
            Node currentItem = Node.this;

            @Override
            public boolean hasNext() {
                while (!dirStack.isEmpty() && index >= getNodeSize()) {
                    Map.Entry<Node, Integer> entry = dirStack.pop();
                    setState(entry.getKey(), entry.getValue());
                }
                if (dirStack.isEmpty() && index >= getNodeSize())
                    return false;
                else
                    return true;
            }

            private void setState(Node key, Integer position) {
                index = position;
                currentItem = key;
            }

            @Override
            public Node next() {
                if (index >= getNodeSize()) {
                    Map.Entry<Node, Integer> entry = dirStack.pop();
                    setState(entry.getKey(), entry.getValue());
                    return next();
                }
                if (getNode().isContrainer()) {
                    Node tempNode = currentItem;
                    dirStack.push(new HashMap.SimpleEntry<Node, Integer>(currentItem, index + 1));
                    setState(getChild(index), 0);
                    if (getNodeSize() == 0) {
                        return currentItem;
                    }
                    if (!dirStack.contains(currentItem)) {
                        return next();
                    } else {
                        return tempNode;
                    }
                }
                if (index < getNodeSize()) {
                    return getChild(index++);
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

        return it;
    }

}
