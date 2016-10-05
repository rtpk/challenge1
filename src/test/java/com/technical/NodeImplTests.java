package com.technical;

import com.technical.models.NodeImpl;
import org.junit.Test;

import java.io.File;

/**
 * Created by Robert Piotrowski on 04/10/2016.
 */
public class NodeImplTests {


    @Test
    public void testIteratorExists() {
        File file = new File("C:/Users/rtpk/Downloads");

        NodeImpl nodes = new NodeImpl(file);

        File file2 = new File( nodes.iterator().next().getNodeName());
        NodeImpl node2 = new NodeImpl(file2);

        node2.iterator().forEachRemaining( p -> System.out.println(p.getNodeName()));
    }
}
