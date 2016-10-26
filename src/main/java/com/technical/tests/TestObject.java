package com.technical.tests;

import java.util.ArrayList;
import java.util.List;

public class TestObject {
    private String name;
    private List<TestObject> list;

    private TestObject() {
    }

    public String getName() {
        return name;
    }

    public static TestObject build() {
        return new TestObject();
    }

    public List<TestObject> getList() {
        return list;
    }

    public TestObject setName(String name) {
        this.name = name;
        return this;
    }

    public TestObject add(TestObject testObject) {
        if(list == null) list = new ArrayList<>();
        list.add(testObject);
        return this;
    }

}
