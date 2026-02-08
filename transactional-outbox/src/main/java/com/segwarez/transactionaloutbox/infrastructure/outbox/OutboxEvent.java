package com.segwarez.transactionaloutbox.infrastructure.outbox;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OutboxEvent {
    private final UUID eventId;
    private final String eventType;
    private final UUID postId;
    private final LocalDateTime createdAt;
}
