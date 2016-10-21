package com.technical.node;

interface NodeWrapper {

  //  List getChildren();  //trzymanie jako tablica w wrapperze ?

    Node getChild(int index);

    int getSize();

    boolean isBranch();
}
