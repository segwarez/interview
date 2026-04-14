package com.segwarez.cache.worker.infrastructure.writebehind;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.segwarez.cache.worker.domain.Event;
import com.segwarez.cache.worker.infrastructure.repository.JPAEventRepository;
import com.segwarez.cache.worker.infrastructure.repository.entity.EventEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.RedisStreamCommands;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    private static final int POLL_COUNT = 250;
    private static final int RECOVERY_COUNT = 50;
    private static final Duration MIN_IDLE_TIME = Duration.ofSeconds(30);

    private final StringRedisTemplate stringRedisTemplate;
    private final JPAEventRepository jpaEventRepository;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 100)
    public void poll() {
        var records = stringRedisTemplate.<String, String>opsForStream()
                .read(
                        Consumer.from(GROUP_NAME, CONSUMER_NAME),
                        StreamReadOptions.empty().count(POLL_COUNT),
                        StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed())
                );

        process(records);
    }

    @Scheduled(fixedDelay = 2000)
    public void recover() {
        var pending = stringRedisTemplate.opsForStream()
                .pending(
                        STREAM_KEY,
                        GROUP_NAME,
                        Range.unbounded(),
                        RECOVERY_COUNT
                );

        if (pending.isEmpty()) return;

        List<RecordId> ids = pending.stream()
                .map(PendingMessage::getId)
                .toList();

        var records = stringRedisTemplate.<String, String>opsForStream()
                .claim(
                        STREAM_KEY,
                        GROUP_NAME,
                        CONSUMER_NAME,
                        RedisStreamCommands.XClaimOptions
                                .minIdle(MIN_IDLE_TIME)
                                .ids(ids.toArray(RecordId[]::new))
                );

        process(records);
    }

    private void process(List<MapRecord<String, String, String>> records) {
        var processed = records.stream()
                .map(this::toEntry)
                .flatMap(Optional::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (processed.isEmpty()) {
            return;
        }

        jpaEventRepository.saveAll(processed.values());

        RecordId[] ids = processed.keySet().toArray(RecordId[]::new);
        stringRedisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP_NAME, ids);
    }

    private Optional<Map.Entry<RecordId, EventEntity>> toEntry(
            MapRecord<String, String, String> record
    ) {
        return toEventEntity(record)
                .map(entity -> Map.entry(record.getId(), entity));
    }

    private Optional<EventEntity> toEventEntity(MapRecord<String, String, String> record) {
        try {
            var jsonEvent = record.getValue().get("event");

            if (jsonEvent == null) {
                return Optional.empty();
            }

            var event = objectMapper.readValue(jsonEvent, Event.class);
            return Optional.of(new EventEntity(event));
        } catch (Exception e) {
            log.error("Failed to parse record {}", record, e);
            return Optional.empty();
        }
    }
}