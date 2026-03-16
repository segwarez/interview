package com.segwarez.observability.database.infrastructure.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "message")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class MessageEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private String message;

    public MessageEntity(UUID id, String message) {
        this.id = id;
        this.message = message;
    }
}