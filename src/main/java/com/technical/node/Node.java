package com.technical.node;

import javax.validation.constraints.NotNull;

public interface Node<T> {
    /**
     * Returns array of children nodes for given node.
     * Cannot be null, if case of not having children return empty array.
     * @return array of nodes
     */
    @NotNull
    Node<T>[] getArray();

    T getPayload();

}
