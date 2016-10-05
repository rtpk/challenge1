package com.technical.node;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Robert Piotrowski on 04/10/2016.
 */
public class NodeImpl implements Node {

    private File file;
    private List<NodeImpl> childsList;

    public NodeImpl(File file) {
        this.file = file;
        this.childsList = new ArrayList<>();
        if (checkIfParent())
            for(File item: file.listFiles()) {
                childsList.add(new NodeImpl(item));
            }
    }

    @Override
    public Iterator<Node> iterator() {
        Iterator<Node> it = new Iterator<Node>() {
            int index = 0;

            @Override
            public boolean hasNext() {
              return (index < childsList.size());

            }

            @Override
            public Node next() {
              //  if(!checkIfParent())
                    return childsList.get(index++);
//                else
//                    return childsList.get(index).iterator().next();
            }
        };

        return it;
    }

    @Override
    public boolean checkIfParent() {
        return (file.isDirectory() && file.list().length > 0);
    }

    @Override
    public String getNodeName() {
        return this.getFile().getAbsolutePath();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
