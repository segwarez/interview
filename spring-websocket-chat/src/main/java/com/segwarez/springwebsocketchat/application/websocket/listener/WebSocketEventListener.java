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

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private static final String CHANNEL = "channel";
    private static final String USERNAME = "username";
    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleConnectedEvent(SessionConnectedEvent sessionConnectedEvent) {
        var headerAccessor = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());
        GenericMessage<?> connectionMessage = (GenericMessage<?>) headerAccessor.getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);

        Optional.ofNullable(connectionMessage)
                .map(SimpMessageHeaderAccessor::wrap)
                .ifPresent(messageHeaderAccessor -> {
                    Optional<String> channelHeader = getValueFromHeader(messageHeaderAccessor, CHANNEL);
                    Optional<String> usernameHeader = getValueFromHeader(messageHeaderAccessor, USERNAME);

                    if (channelHeader.isPresent() && usernameHeader.isPresent()) {
                        var channel = channelHeader.get();
                        var username = usernameHeader.get();

                        messageHeaderAccessor.getSessionAttributes().put(CHANNEL, channel);
                        messageHeaderAccessor.getSessionAttributes().put(USERNAME, username);

                        messagingTemplate.convertAndSend(String.format("/topic/public/%s", channel),
                                ChatEvent.builder()
                                        .eventType(EventType.JOINED)
                                        .user(username)
                                        .build());
                    }
                });
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent) {
        var headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        Optional<String> channelAttribute = getSessionAttribute(headerAccessor, CHANNEL);
        Optional<String> usernameAttribute = getSessionAttribute(headerAccessor, USERNAME);

        if (channelAttribute.isPresent() && usernameAttribute.isPresent()) {
            messagingTemplate.convertAndSend(String.format("/topic/public/%s", channelAttribute.get()),
                    ChatEvent.builder()
                            .eventType(EventType.LEFT)
                            .user(usernameAttribute.get())
                            .build());
        }
    }

    private Optional<String> getValueFromHeader(SimpMessageHeaderAccessor headerAccessor, String header) {
        return Optional.ofNullable(headerAccessor.getNativeHeader(header)).map(headers -> headers.getFirst());
    }

    private Optional<String> getSessionAttribute(SimpMessageHeaderAccessor headerAccessor, String attribute) {
        return Optional.ofNullable(headerAccessor.getSessionAttributes().get(attribute)).map(Object::toString);
    }
}
