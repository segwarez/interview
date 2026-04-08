package com.segwarez.cache.api.infrastructure.repository.entity;

import com.segwarez.cache.api.domain.Event;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "events")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class EventEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private String type;
    private String userId;
    private Instant timestamp;
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload;

    public EventEntity(Event event) {
        this.id = event.getId();
        this.type = event.getType();
        this.userId = event.getUserId();
        this.timestamp = event.getTimestamp();
        this.payload = event.getPayload();
    }

    public Event toDomain() {
        return new Event(id, type, userId, timestamp, payload);
    }
}