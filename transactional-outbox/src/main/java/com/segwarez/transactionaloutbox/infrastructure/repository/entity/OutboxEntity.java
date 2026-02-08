package com.segwarez.transactionaloutbox.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "outbox")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class OutboxEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID eventId;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private UUID postId;
    private Boolean processed = false;
    @CreatedDate
    private LocalDateTime createdAt;

    public OutboxEntity(EventType eventType, UUID postId) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.postId = postId;
    }
}