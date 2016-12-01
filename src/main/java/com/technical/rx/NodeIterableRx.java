package com.technical.rx;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;

@Slf4j
public class NodeIterableRx<Path> {

    public Observable<Path> convert(Iterable<Path> source) {
        return Observable.from(source);
    }

}

