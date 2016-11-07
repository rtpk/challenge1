package com.technical;

import com.technical.node.Node;
import com.technical.node.NodeIterable;
import com.technical.tests.NodeTestImpl;
import com.technical.tests.TestObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

public class NodeIterableTest {


    @Test
    public void shouldHasNextIsFalseNoMoreElements() {
        //Given:
        TestObject rootParent = TestObject.build().setName("file");
        NodeIterable<NodeTestImpl> root = new NodeIterable<>(new NodeTestImpl(rootParent));

        //When:
        Iterator<NodeTestImpl> it = root.iterator();

        //Then:
        assertThat(it.hasNext()).isFalse();
    }


    @Test
    public void shouldHasNextIsTrueOneElement() {
        //Given:
        TestObject rootParent = TestObject.build();
        rootParent.add(TestObject.build().setName("leaf"));
        NodeIterable<NodeTestImpl> root = new NodeIterable<>(new NodeTestImpl(rootParent));

        //When:
        Iterator<NodeTestImpl> it = root.iterator();

        //Then:
        assertThat(it.hasNext()).isTrue();
    }


    @Test
    public void shouldWorkWithTwoSeparateIterators() {
        //Given:
        TestObject testObj = TestObject.build();
        testObj.add(TestObject.build().setName("branch")).add(TestObject.build().setName("leaf"));
        NodeIterable<NodeTestImpl> root = new NodeIterable<>(new NodeTestImpl(testObj));

        //When:
        Iterator<NodeTestImpl> firstIterator = root.iterator();
        Iterator<NodeTestImpl> secondIterator = root.iterator();

        Node objFirstIterator = firstIterator.next();
        Node objSecondIterator = secondIterator.next();

        //Then:
        assertThat(objFirstIterator.getPayload()).isEqualTo(objSecondIterator.getPayload());
    }


    @Test
    public void shouldIterateWithoutHasNext() {
        //Given:
        TestObject testObj = TestObject.build().setName("root");
        testObj.add(TestObject.build());
        NodeIterable<NodeTestImpl> root = new NodeIterable<>(new NodeTestImpl(testObj));

        //Then:
        assertThat(root.iterator().next().getPayload()).isEqualTo("null");
    }

    @Test
    public void shouldIterateOnComplexTree() {
        //Given:
        TestObject testObj = TestObject.build().setName("root");
        testObj.add(TestObject.build().setName("branch1").add(TestObject.build().setName("leaf1")));
        testObj.add(TestObject.build().setName("branch2").add(TestObject.build().setName("branch4").add(TestObject.build().setName("leaf5"))));
        testObj.add(TestObject.build().setName("branch3").add(TestObject.build().setName("leaf3")));
        testObj.add(TestObject.build().setName("leaf4"));
        NodeIterable<NodeTestImpl> root = new NodeIterable<>(new NodeTestImpl(testObj));

        //When:
        Iterator<NodeTestImpl> it = root.iterator();
        List<String> paths = new ArrayList<>();
        while (it.hasNext()) {
            Node temp = it.next();
            paths.add(temp.getPayload());
        }

        //Then
        assertThat(paths).contains("branch1",
                "leaf1",
                "branch2",
                "branch4",
                "leaf5",
                "branch3",
                "leaf3",
                "leaf4");
    }



    @Test
    public void shouldIterateOnComplexTreeOnStreams() {
        //Given:
        TestObject testObj = TestObject.build().setName("root");
        testObj.add(TestObject.build().setName("branch1").add(TestObject.build().setName("leaf1")));
        testObj.add(TestObject.build().setName("branch2").add(TestObject.build().setName("branch4").add(TestObject.build().setName("leaf5"))));
        testObj.add(TestObject.build().setName("branch3").add(TestObject.build().setName("leaf3")));
        testObj.add(TestObject.build().setName("leaf4"));
        NodeIterable<NodeTestImpl> root = new NodeIterable<>(new NodeTestImpl(testObj));

        //When:
        Iterator<NodeTestImpl> it = root.iterator();
        @SuppressWarnings("unchecked")
        List<String> results = (List<String>) StreamSupport.stream(root.spliterator(), false).map(o -> ((Node)o).getPayload()).collect(Collectors.toList());

        //Then
        assertThat(it).extracting(p -> (p).getPayload()).isEqualTo(results);
    }




}
