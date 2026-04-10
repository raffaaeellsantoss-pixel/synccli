package com.synccli.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ClipboardController {

    @MessageMapping("/send")
    @SendTo("/topic/clipboard")
    public String send(String message) {
        System.out.println("Recebido: " + message);
        return message;
    }
}