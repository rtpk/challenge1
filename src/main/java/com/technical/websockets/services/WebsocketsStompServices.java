package com.technical.websockets.services;


import com.technical.services.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
class WebsocketsStompServices {

    private final SimpMessagingTemplate template;
    private final Utils utils;

    @Autowired
    public WebsocketsStompServices(SimpMessagingTemplate template, Utils utils) {
        this.template = template;
        this.utils = utils;
    }

    @MessageMapping("/files")
    public void addFile(String pathName) throws Exception {
     utils.createFileByPath(pathName);
    }

    public void sendFilesListing(String content) {
        this.template.convertAndSend("/filesList", content);
    }

}
