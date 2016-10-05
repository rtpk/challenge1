package com.technical.websockets.services;


import com.technical.services.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Created by Robert Piotrowski on 23/08/2016.
 */

@Controller
public class WebsocketsStompServices {

    private final SimpMessagingTemplate template;
    private final Utils utils;

    @Autowired
    public WebsocketsStompServices(SimpMessagingTemplate template, Utils utils) {
        this.template = template;
        this.utils = utils;
    }

    public void sendQuotations(String content) {
        this.template.convertAndSend("/topic/files", content);
    }

    @MessageMapping("/files")
    @SendTo("/topic/files")
    public String checkUpdates(String name) throws Exception {
        return String.valueOf(utils.createFileByPath(name));
    }
}
