package com.segwarez.springwebsocketchat.application.websocket;

import com.segwarez.springwebsocketchat.domain.ChatEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendMessage/{channel}")
    public ChatEvent sendMessage(@Payload ChatEvent chatEvent, @DestinationVariable("channel") String channel) {
        messagingTemplate.convertAndSend(String.format("/topic/public/%s", channel), chatEvent);
        return chatEvent;
    }
}
