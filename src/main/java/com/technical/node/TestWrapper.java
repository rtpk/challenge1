package com.technical.node;

import java.util.ArrayList;
import java.util.List;

public class TestWrapper implements NodeWrapper {

    private TestObject testObject;
    private List<TestObject> objects = new ArrayList<>();

    public TestWrapper(TestObject testObject) {
        this.testObject = testObject;
        this.objects = testObject.getList();
    }

    @Override
    public Node getChild(int index) {
        return new NodeImpl(new TestWrapper(objects.get(index)));
    }

    @Override
    public int getSize() {
        if (!testObject.isBranch()) return 0;
        return objects.size();
    }

    @Override
    public boolean isBranch() {
        return testObject.isBranch();
    }

    @Override
    public String toString() {
        return testObject.toString();
    }

}
