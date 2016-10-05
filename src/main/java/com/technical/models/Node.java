package com.technical.models;

/**
 * Created by Robert Piotrowski on 04/10/2016.
 */
public interface Node extends Iterable<Node> {

    boolean checkIfParent();
    String getNodeName();

}
