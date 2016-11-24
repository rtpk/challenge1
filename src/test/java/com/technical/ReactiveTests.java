package com.technical;

import com.technical.file.NodeFile;
import com.technical.node.NodeIterable;
import com.technical.rx.NodeIterableRx;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import rx.Observable;
import rx.observables.BlockingObservable;

import java.io.File;

@Slf4j
public class ReactiveTests {

    private void print(File obj) {
        log.info("Got: {}", obj.getAbsoluteFile());
    }

    @Test
    public void testNodeIterableRxFiles() {
        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder");
        NodeIterable<File> root = new NodeIterable<>(new NodeFile(file));
        NodeIterableRx<File> temp = new NodeIterableRx<>();
        BlockingObservable<File> result = temp.convert2(root);
        result.subscribe(this::print);
    }

    @Test
    public void testNodeIterableRxTestObjects() {
        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder");
        NodeIterable<File> root = new NodeIterable<>(new NodeFile(file));
        NodeIterableRx<File> temp = new NodeIterableRx<>();
        Observable<File> result = temp.convert(root);

        result.subscribe(
                element -> {
                    System.out.println(element.toString());
                },
                System.err::println,
                () -> {
                    System.out.println("We've finnished!");
                });
        result.publish();
    }

    @Test
    public void testNodeIterableRxTestObjects2() {
        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder");
        NodeIterable<File> root = new NodeIterable<>(new NodeFile(file));
        NodeIterableRx<File> temp = new NodeIterableRx<>();
        Observable<File> result = temp.fromIterable(root);

        result.distinct().subscribe(
                element -> {
                    System.out.println(element.toString());
                },
                System.err::println,
                () -> {
                    System.out.println("We've finnished!");
                });
    }
}
