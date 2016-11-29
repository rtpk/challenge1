package com.technical.rx;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;

@Slf4j
public class NodeIterableRx<T> {

    public Observable<T> convert(Iterable<T> source) {
        return Observable.from(source);
    }

}

