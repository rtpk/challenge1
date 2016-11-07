package com.technical;

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
        NodeIterable<String> root = new NodeIterable<>(new NodeTestImpl(rootParent));

        //When:
        Iterator<String> it = root.iterator();

        //Then:
        assertThat(it.hasNext()).isFalse();
    }


    @Test
    public void shouldHasNextIsTrueOneElement() {
        //Given:
        TestObject rootParent = TestObject.build();
        rootParent.add(TestObject.build().setName("leaf"));
        NodeIterable<String> root = new NodeIterable<>(new NodeTestImpl(rootParent));

        //When:
        Iterator<String> it = root.iterator();

        //Then:
        assertThat(it.hasNext()).isTrue();
    }


    @Test
    public void shouldWorkWithTwoSeparateIterators() {
        //Given:
        TestObject testObj = TestObject.build();
        testObj.add(TestObject.build().setName("branch")).add(TestObject.build().setName("leaf"));
        NodeIterable<String> root = new NodeIterable<>(new NodeTestImpl(testObj));

        //When:
        Iterator<String> firstIterator = root.iterator();
        Iterator<String> secondIterator = root.iterator();

        String objFirstIterator = firstIterator.next();
        String objSecondIterator = secondIterator.next();

        //Then:
        assertThat(objFirstIterator).isEqualTo(objSecondIterator);
    }


    @Test
    public void shouldIterateWithoutHasNext() {
        //Given:
        TestObject testObj = TestObject.build().setName("root");
        testObj.add(TestObject.build());
        NodeIterable<String> root = new NodeIterable<>(new NodeTestImpl(testObj));

        //Then:
        assertThat(root.iterator().next()).isEqualTo("null");
    }

    @Test
    public void shouldIterateOnComplexTree() {
        //Given:
        TestObject testObj = TestObject.build().setName("root");
        testObj.add(TestObject.build().setName("branch1").add(TestObject.build().setName("leaf1")));
        testObj.add(TestObject.build().setName("branch2").add(TestObject.build().setName("branch4").add(TestObject.build().setName("leaf5"))));
        testObj.add(TestObject.build().setName("branch3").add(TestObject.build().setName("leaf3")));
        testObj.add(TestObject.build().setName("leaf4"));
        NodeIterable<String> root = new NodeIterable<>(new NodeTestImpl(testObj));

        //When:
        Iterator<String> it = root.iterator();
        List<String> paths = new ArrayList<>();
        while (it.hasNext()) {
            String temp = it.next();
            paths.add(temp);
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
        NodeIterable<String> root = new NodeIterable<>(new NodeTestImpl(testObj));

        //When:
        Iterator<String> it = root.iterator();
        @SuppressWarnings("unchecked")
        List<String> results = (List<String>) StreamSupport.stream(root.spliterator(), false).map(o -> (o)).collect(Collectors.toList());

        //Then
        assertThat(it).extracting(p -> (p)).isEqualTo(results);
    }




}
