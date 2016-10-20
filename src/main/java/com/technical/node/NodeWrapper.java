package com.technical.node;

/**
 * Created by Robert Piotrowski on 19/10/2016.
 */
public interface NodeWrapper {
    public Node getChild(int index);

    public int getSize();

    public boolean isContrainer();
}
