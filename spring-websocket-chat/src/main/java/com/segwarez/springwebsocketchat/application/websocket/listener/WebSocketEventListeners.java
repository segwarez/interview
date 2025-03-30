package com.segwarez.springwebsocketchat.application.websocket.listener;

import com.segwarez.springwebsocketchat.domain.ChatEvent;
import com.segwarez.springwebsocketchat.domain.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class WebSocketEventListeners {
    private static final String USERNAME_HEADER = "username";
    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleConnectedEvent(SessionConnectedEvent sessionConnectedEvent) {
        var headerAccessor = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());
        GenericMessage<?> generic = (GenericMessage<?>) headerAccessor.getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
        if (nonNull(generic)) {
            SimpMessageHeaderAccessor messageHeaderAccessor = SimpMessageHeaderAccessor.wrap(generic);
            Optional<String> username = Optional.ofNullable(messageHeaderAccessor.getNativeHeader(USERNAME_HEADER))
                    .flatMap(headers -> Optional.ofNullable(headers.getFirst()));
            username.ifPresent(user -> {
                messageHeaderAccessor.getSessionAttributes().put(USERNAME_HEADER, user);
                messagingTemplate.convertAndSend("/topic/public",
                        ChatEvent.builder()
                                .eventType(EventType.JOINED)
                                .user(user)
                                .build());
            });
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        Optional<String> username = Optional.ofNullable(headerAccessor.getSessionAttributes().get(USERNAME_HEADER).toString());
        username.ifPresent(userLeft ->
                messagingTemplate.convertAndSend("/topic/public",
                        ChatEvent.builder()
                                .eventType(EventType.LEFT)
                                .user(userLeft)
                                .build()));
    }
}
