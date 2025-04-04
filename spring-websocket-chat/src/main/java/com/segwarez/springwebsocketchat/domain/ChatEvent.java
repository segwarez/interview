package com.segwarez.springwebsocketchat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChatEvent {
    private String username;
    private String content;
    private EventType eventType;
}
