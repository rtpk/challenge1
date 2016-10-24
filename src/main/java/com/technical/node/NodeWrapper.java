package com.technical.node;

interface NodeWrapper {

    Node getChild(int index);

    int getSize();

    boolean isBranch();
}
