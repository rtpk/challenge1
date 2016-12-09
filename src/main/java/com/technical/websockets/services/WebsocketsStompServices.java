package com.technical.websockets.services;


import com.technical.rx.PathRx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import rx.Observable;
import rx.subjects.ReplaySubject;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.WatchEvent;

@Slf4j
@Controller
class WebsocketsStompServices {

    private final SimpMessagingTemplate template;
    private FileSystem fileSystem;
    private Observable<WatchEvent<?>> observable;

    @Autowired
    public WebsocketsStompServices(SimpMessagingTemplate template, FileSystem fileSystem) {
        this.template = template;
        this.fileSystem = fileSystem;
    }

    @PostConstruct
    public void init()  throws Exception {
        System.out.println("START");
        Files.createDirectories(fileSystem.getPath("/root"));
        observable = PathRx.watch(fileSystem.getPath("/root"));
        ReplaySubject<WatchEvent<?>> replaySubject = ReplaySubject.create();  //jakas tablica
        observable.subscribe(replaySubject);
        replaySubject.subscribe(
                element -> {sendFilesListing(element.toString());
                    System.out.println("wyslano");});
    }

    @MessageMapping("/start")
    public void start(String pathName) throws InterruptedException, IOException {


        Files.createDirectories(fileSystem.getPath("/root/test"));
    }


    @MessageMapping("/files")
    public void addFile(String pathName) throws Exception {
        System.out.println("przyszlo: " +pathName);
        Files.createDirectories(fileSystem.getPath(pathName));
    }

    public void sendFilesListing(String content) {
        this.template.convertAndSend("/filesList", content);
    }

}
