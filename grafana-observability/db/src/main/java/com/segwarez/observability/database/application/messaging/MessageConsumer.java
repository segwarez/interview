package com.segwarez.observability.database.application.messaging;

import com.segwarez.observability.database.infrastructure.repository.JPAMessageRepository;
import com.segwarez.observability.database.infrastructure.repository.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.segwarez.observability.database.infrastructure.configuration.KafkaConfig.DATABASE_TOPIC;

@Component
@RequiredArgsConstructor
public class MessageConsumer {
    private final JPAMessageRepository messageRepository;

    @KafkaListener(topics = DATABASE_TOPIC, groupId = "observability-database-consumer")
    public void consume(ConsumerRecord<String, String> event) {
        messageRepository.save(new MessageEntity(UUID.fromString(event.key()), event.value()));
    }
}