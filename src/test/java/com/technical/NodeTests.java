package com.technical;

import com.google.common.collect.Lists;
import com.technical.node.Node;
import com.technical.node.NodeImpl;
import com.technical.node.TestObject;
import com.technical.node.TestWrapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NodeTests {


    @Test
    public void shouldHasNextEmptyCollection() {
        TestObject testObj = TestObject.build();
        Node root = new NodeImpl(new TestWrapper(testObj));
        Iterator it = root.iterator();
        assertThat(it.hasNext()).isFalse();
    }


    @Test
    public void shouldHasNextOneElementCollection() {
        TestObject testObj = TestObject.build();
        testObj.add(TestObject.build());
        Node root = new NodeImpl(new TestWrapper(testObj));
        Iterator it = root.iterator();
        assertThat(it.hasNext()).isTrue();
    }


    @Test
    public void shouldMoreThanOneIteratorOnTestObjects() {
        TestObject testObj = TestObject.build();
        testObj.add(TestObject.build()).add(TestObject.build());
        Node root = new NodeImpl(new TestWrapper(testObj));

        Iterator firstIterator = root.iterator();
        Iterator secondIterator = root.iterator();

        assertThat(firstIterator.next().toString()).isEqualTo(secondIterator.next().toString());
    }

//TODO fix toString()
    @Test
    public void shouldIterateWithoutHasNext() {
        TestObject testObj = TestObject.build().setName("test");
        testObj.add(TestObject.build());
        Node root = new NodeImpl(new TestWrapper(testObj));
        assertThat(root.iterator().next()).isNull();  //assertj  nie dziala
    }

    @Test
    public void shouldIteratorOnTestObjects() {

        TestObject testObj = TestObject.build().setName("root");
        testObj.add(TestObject.build().setName("branch1").add(TestObject.build().setName("leaf1")));
        testObj.add(TestObject.build().setName("branch2").add(TestObject.build().setName("branch4").add(TestObject.build().setName("leaf5"))));
        testObj.add(TestObject.build().setName("branch3").add(TestObject.build().setName("leaf3")));
        testObj.add(TestObject.build().setName("leaf4"));

        Node root = new NodeImpl(new TestWrapper(testObj));

        Iterator it = root.iterator();

        List<String> paths = new ArrayList<>();

        while (it.hasNext()) {
            paths.add(it.next().toString());
        }

        assertThat(paths).isEqualTo(Lists.newArrayList("branch1",
                "leaf1",
                "branch2",
                "branch4",
                "leaf5",
                "branch3",
                "leaf3",
                "leaf4"));

//        assertThat(paths).contains("branch1",
//                "leaf1",
//                "branch2",
//                "branch4",
//                "leaf5",
//                "branch3",
//                "leaf3",
//                "leaf4");
    }

}
