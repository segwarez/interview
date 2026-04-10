package com.segwarez.cache.worker.infrastructure.repository.entity;

import com.segwarez.cache.worker.domain.Event;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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
}