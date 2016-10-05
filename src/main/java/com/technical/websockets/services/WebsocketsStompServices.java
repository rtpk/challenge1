package com.technical.websockets.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Created by Robert Piotrowski on 23/08/2016.
 */

@Controller
public class WebsocketsStompServices {

    private final SimpMessagingTemplate template;

    @Autowired
    public WebsocketsStompServices(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendQuotations(String content) {
        this.template.convertAndSend("/topic/files", content);
    }
}
