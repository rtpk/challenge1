package com.technical.node;

import javax.validation.constraints.NotNull;

public interface Node {

    /**
     * Returns array.
     * @return array of Nodes
     */
    @NotNull
    Node[] getArray();

    String getPayload();

}
