package com.technical.websockets.services;


import com.technical.file.NodeFile;
import com.technical.node.NodeIterable;
import com.technical.rx.NodeIterableRx;
import com.technical.services.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import rx.observables.BlockingObservable;

import java.io.File;

@Slf4j
@Controller
class WebsocketsStompServices {

    private final SimpMessagingTemplate template;
    private final Utils utils;

    @Autowired
    public WebsocketsStompServices(SimpMessagingTemplate template, Utils utils) {
        this.template = template;
        this.utils = utils;
    }

    @MessageMapping("/start")
    public void start(String pathName) throws Exception {
        File file = new File("C:\\Users\\rtpk\\Downloads\\New folder");
        NodeIterable<File> root = new NodeIterable<>(new NodeFile(file));
        NodeIterableRx<File> temp = new NodeIterableRx<>();
        BlockingObservable<File> result = temp.convert2(root);
        result.subscribe(
                element -> {
                    sendFilesListing(element.toString());
                });
    }


    @MessageMapping("/files")
    public void addFile(String pathName) throws Exception {
        utils.createFileByPath(pathName);
    }

    public void sendFilesListing(String content) {
        log.info("WYSLANO");
        this.template.convertAndSend("/filesList", content);
    }

}
