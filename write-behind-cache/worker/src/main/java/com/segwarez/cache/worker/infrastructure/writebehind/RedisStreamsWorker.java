package com.segwarez.cache.worker.infrastructure.writebehind;

import com.segwarez.cache.worker.infrastructure.repository.JPAEventRepository;
import com.segwarez.cache.worker.infrastructure.repository.entity.EventEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisStreamsWorker {
    private static final String STREAM_KEY = "WRITE_BEHIND_STREAM";
    private static final String GROUP_NAME = "write_behind_group";
    private static final String CONSUMER_NAME = "worker_" + UUID.randomUUID();

    private final StringRedisTemplate stringRedisTemplate;
    private final JPAEventRepository jpaEventRepository;

    @Scheduled(fixedDelay = 100)
    public void processInBatch() {
        List<MapRecord<String, String, String>> records = stringRedisTemplate
                .<String, String>opsForStream()
                .read(
                        Consumer.from(GROUP_NAME, CONSUMER_NAME),
                        StreamReadOptions.empty().count(100),
                        StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed())
                );

        Map<RecordId, EventEntity> mapped = records.stream()
                .map(r -> toEntry(r))
                .flatMap(Optional::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (mapped.isEmpty()) return;

        jpaEventRepository.saveAll(mapped.values());

        RecordId[] ids = mapped.keySet().toArray(RecordId[]::new);
        stringRedisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP_NAME, ids);
    }

    private Optional<Map.Entry<RecordId, EventEntity>> toEntry(MapRecord<String, String, String> record) {
        return toEventEntity(record).map(entity -> Map.entry(record.getId(), entity));
    }

    private Optional<EventEntity> toEventEntity(MapRecord<String, String, String> record) {
        try {
            var value = record.getValue();
            return Optional.of(new EventEntity(
                    UUID.fromString(value.get("id")),
                    value.get("type"),
                    value.get("userId"),
                    Instant.parse(value.get("timestamp")),
                    value.get("payload")
            ));
        } catch (Exception e) {
            log.error("Failed to parse record {}", record, e);
            return Optional.empty();
        }
    }
}