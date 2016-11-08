package com.technical;

import com.technical.file.NodeFile;
import com.technical.node.NodeIterable;
import com.technical.rx.NodeIterableRx;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import rx.observables.BlockingObservable;

import java.io.File;

@Slf4j
public class ReactiveTests {

//    @Test
//    public void testIteratorExists() {
//        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder");
//        NodeIterable<File> root = new NodeIterable<>(new NodeFile(file));
//        Iterator<File> it = root.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next().getAbsoluteFile());
//        }
//    }

    private void print(File obj) {
        log.info("Got: {}", obj.getAbsoluteFile());
    }

    @Test
    public void testNodeIterableRxFiles() {
        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder");
        NodeIterable<File> root = new NodeIterable<>(new NodeFile(file));
        NodeIterableRx<File> temp = new NodeIterableRx<>();
        BlockingObservable<File> result = temp.convert(root);
        result.subscribe(this::print);
    }

    @Test
    public void testNodeIterableRxTestObjects() {
        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder");
        NodeIterable<File> root = new NodeIterable<>(new NodeFile(file));
        NodeIterableRx<File> temp = new NodeIterableRx<>();
        BlockingObservable<File> result = temp.convert(root);
        result.subscribe(this::print);
    }


//
//
//
//    List<File> childrenOf(NodeIterable<File> root) {
//        Iterator<File> it = root.iterator();
//        List<File> results = (List<File>) StreamSupport.stream(root.spliterator(), false).map(o -> (o)).collect(Collectors.toList());
//        return results;
//    }
//
//
//    @Test
//    public void testrx() {
//        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder");
//        NodeIterable<File> root = new NodeIterable<>(new NodeFile(file));
//        Observable
//                .interval(1, TimeUnit.SECONDS)
//                .concatMapIterable(x -> childrenOf(root))
//                .distinct()
//                .toBlocking()
//                .subscribe(this::print);
//    }

}
