package com.segwarez.transactionaloutbox.infrastructure.outbox;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@AllArgsConstructor
public class OutboxEvent {
   UUID eventId;
   String eventType;
   UUID postId;
   LocalDateTime createdAt;
}
