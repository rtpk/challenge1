package com.technical.rx;

import com.technical.node.NodeIterable;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.observables.BlockingObservable;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class NodeIterableRx<T> {

    public Observable<T> convert(Iterable<T> source) {
        Observable<T> result = Observable.from(source);
        result.distinct();
        return result;
    }

    public Observable<T> fromIterable(final Iterable<T> iterable) {
        return Observable.create(
                subscriber -> {
                    try {
                        Iterator<T> iterator = iterable.iterator();
                        while (!subscriber.isUnsubscribed()) {
                            if (iterator.hasNext())
                                subscriber.onNext(iterator.next());
                            else iterator = iterable.iterator();
                        }
                    } catch (Exception e) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(e);
                        }
                    }
                });
    }

    public BlockingObservable<T> convert2(NodeIterable<T> source) {
        return Observable.interval(1, TimeUnit.SECONDS)
                .concatMapIterable(x -> childrenOf(source))
                .distinct()
                .toBlocking();
    }

    private List<T> childrenOf(NodeIterable<T> root) {
        return (List<T>) StreamSupport.stream(root.spliterator(), false).collect(Collectors.toList());
    }

    private void print(File obj) {
        log.info("Got: {}", obj.getAbsoluteFile());
    }

}

