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
    public String checkUpdates(String pathName) throws Exception {
        return String.valueOf(utils.createFileByPath(pathName));
    }
}
