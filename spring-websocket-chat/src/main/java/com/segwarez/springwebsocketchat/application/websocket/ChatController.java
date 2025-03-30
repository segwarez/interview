package com.segwarez.springwebsocketchat.application.websocket;

import com.segwarez.springwebsocketchat.domain.ChatEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatEvent sendMessage(@Payload ChatEvent chatEvent) {
        return chatEvent;
    }
}
