package com.technical.rx;

import com.technical.file.NodeFile;
import com.technical.node.NodeIterable;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.subjects.ReplaySubject;

import java.io.File;
import java.io.IOException;

@Slf4j
public class ObservableIteratorMerge<T> {

    public static File createFileByPath(String name) {
        File newFile = new File(name);
        try {
            if (newFile.createNewFile())
                return newFile;
        } catch (IOException e) {
            log.error("Error while creating new file {}", e.getCause().toString());
        }
        return null;

    }

    public static void main(String[] args) {
        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder");
        NodeIterable<File> root = new NodeIterable<>(new NodeFile(file));

        Observable observableInitial = Observable.from(root);
        ReplaySubject<File> subject = ReplaySubject.create();

        Observable<File> source = Observable.merge(observableInitial, subject);

        source.distinct().subscribe(
                element -> {
                    System.out.println(element.toString());
                },
                System.err::println,
                () -> {
                    System.out.println("We've finnished!");
                });

        subject.onNext(createFileByPath("C:\\Users\\rtpk\\Downloads\\New folder\\New folder\\Testy\\test1.txt"));
        subject.onNext(createFileByPath("C:\\Users\\rtpk\\Downloads\\New folder\\New folder\\Testy\\test2.txt"));
    }

}