package com.segwarez.cache.worker.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Serializable {
    private UUID id;
    private String type;
    private String userId;
    private Instant timestamp;
    private String payload;

    public Event(String type, String userId, Instant timestamp, String payload) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.userId = userId;
        this.timestamp = timestamp;
        this.payload = payload;
    }
}