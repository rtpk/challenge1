package com.technical.node;

public interface Node {


    //zabronci null kontrakt  1.@notnull 2.dokumentacja
    Node[] getArray();

    boolean isBranch(); //pozbycSie

    String getPayload();

    int getSize(); //pozbycSie

}
