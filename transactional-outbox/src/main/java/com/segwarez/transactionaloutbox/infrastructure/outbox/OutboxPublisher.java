package com.segwarez.transactionaloutbox.infrastructure.outbox;

import com.segwarez.transactionaloutbox.infrastructure.configuration.KafkaConfig;
import com.segwarez.transactionaloutbox.infrastructure.repository.JPAOutboxRepository;
import com.segwarez.transactionaloutbox.infrastructure.repository.entity.OutboxEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final JPAOutboxRepository outboxRepository;
    private final KafkaTemplate<String, OutboxEvent> kafkaTemplate;

    @Scheduled(fixedRate = 2000)
    public void publish() {
        List<OutboxEntity> outboxEntities = outboxRepository.findTop50ByProcessed(false);

        for (OutboxEntity entity : outboxEntities) {
            var event = new OutboxEvent(
                    entity.getEventId(),
                    entity.getEventType().name(),
                    entity.getPostId(),
                    entity.getCreatedAt());
            kafkaTemplate.send(KafkaConfig.OUTBOX_TOPIC, event);
            entity.setProcessed(true);
            outboxRepository.save(entity);
        }
    }
}
