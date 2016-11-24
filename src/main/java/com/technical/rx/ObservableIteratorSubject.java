package com.technical.rx;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ObservableIteratorSubject<T> {


    public static class ObservableList<T> {

        protected final List<T> list;
        protected final PublishSubject<T> onAdd;

        public ObservableList() {
            this.list = new ArrayList<T>();
            this.onAdd = PublishSubject.create();
        }
        public void add(T value) {
            list.add(value);
            onAdd.onNext(value);
        }
        public Observable<T> getObservable() {
            return onAdd;
        }
    }

    public static void main(String[] args) throws Exception {
        ObservableList<Integer> olist = new ObservableList<>();

        olist.getObservable().subscribe(System.out::println);

        olist.add(1);
        Thread.sleep(1000);
        olist.add(2);
        Thread.sleep(1000);
        olist.add(3);
    }
}