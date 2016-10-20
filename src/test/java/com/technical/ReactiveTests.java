package com.technical;

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
    public void testrx() {
        File tmp = new File("C:\\Users\\rtpk\\Downloads");
        Observable
                .interval(1, TimeUnit.SECONDS)
                .concatMapIterable(x -> childrenOf(tmp))
                .distinct()
                .subscribe(this::print);
    }

    List<String> childrenOf(File file) {
        return Arrays
                .asList(file.listFiles())
                .stream()
                .map(File::getName)
                .collect(Collectors.toList());
    }
    public void print(Object obj) {
        log.info("Got: {}", obj);
    }
}
