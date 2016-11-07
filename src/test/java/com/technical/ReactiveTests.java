package com.technical;

import com.technical.file.NodeFile;
import com.technical.node.Node;
import com.technical.node.NodeIterable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import rx.Observable;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ReactiveTests {

    @Test
    public void testIteratorExists() {
        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder");
        NodeIterable root = new NodeIterable<>(new NodeFile(file));
        for (Object aRoot : root) {
            System.out.println(((Node) aRoot).getPayload());
        }
    }


    @Test
    public void testrx() {
        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder");
        NodeIterable root = new NodeIterable<>(new NodeFile(file));
        Observable
                .interval(1, TimeUnit.SECONDS)
                .from(root::iterator)
                .distinct()
                .toBlocking()
                .subscribe(this::print);
    }

    private void print(Object obj) {
        log.info("Got: {}", ((Node)obj).getPayload());
    }
}
