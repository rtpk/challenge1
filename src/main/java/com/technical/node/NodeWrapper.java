package com.technical.node;

public interface NodeWrapper {

    //public Collections<NodeWrapper> children();  //komentarz: readonly // array

    Node getChild(int index); //zbedne

    int getSize(); //zbedne

    boolean isBranch();
}
