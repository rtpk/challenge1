package com.technical.rx;

import com.technical.node.NodeIterable;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;

import java.nio.file.Path;

@Slf4j
public class NodeIterableRx {

    public Observable<Path> convert(NodeIterable<Path> source) {
        Observable<Path> result = Observable.from(source);
        return result;
    }

}

