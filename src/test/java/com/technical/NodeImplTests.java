package com.technical;

import com.technical.node.Node;
import com.technical.node.NodeImpl;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Java6Assertions.assertThat;


/**
 * Created by Robert Piotrowski on 04/10/2016.
 */
public class NodeImplTests {


    @Test
    public void testIteratorExists() {
        File file = new File("C:/Users/rtpk/Downloads");

        Node nodes = new NodeImpl(file);
        assertThat(nodes.iterator()).isNotNull();
//        File file2 = new File( nodes.iterator().next().getNodeName());
//        NodeImpl node2 = new NodeImpl(file2);
//
//        node2.iterator().forEachRemaining( p -> System.out.println(p.getNodeName()));
    }
}
