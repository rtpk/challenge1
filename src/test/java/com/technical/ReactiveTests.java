package com.technical;

import com.technical.file.NodeFile;
import com.technical.node.Node;
import com.technical.node.NodeIterable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import rx.Observable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Robert Piotrowski on 18/10/2016.
 */
@Slf4j
public class ReactiveTests {

    @Test
    public void testIteratorExists() {
        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder");
        NodeIterable root = new NodeIterable<>(new NodeFile(file));
        for (Object node : root) {
            System.out.println(((Node)node).getPayload());
        }
    }


    @Test
    public void testrx() {
        java.io.File tmp = new java.io.File("C:\\Users\\rtpk\\Downloads");
        Observable
                .interval(1, TimeUnit.SECONDS)
                .concatMapIterable(x -> childrenOf(tmp))
                .distinct()
                .subscribe(this::print);
    }

    private List<String> childrenOf(File file) {
        return Arrays
                .asList(file.listFiles())
                .stream()
                .map(java.io.File::getName)
                .collect(Collectors.toList());
    }
    public void print(Object obj) {
        log.info("Got: {}", obj);
    }
}
