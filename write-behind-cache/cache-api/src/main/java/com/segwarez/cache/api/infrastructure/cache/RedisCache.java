package com.segwarez.cache.api.infrastructure.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.segwarez.cache.api.domain.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCache {
    private static final String STREAM_KEY = "WRITE_BEHIND_STREAM";

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisScript<String> writeBehindScript;
    private final ObjectMapper objectMapper;

    public void save(Event event) {
        String eventJson = null;
        try {
            eventJson = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Error serializing event {}", event, e);
            throw new RuntimeException(e);
        }

        var streamId = stringRedisTemplate.execute(
                writeBehindScript,
                List.of(event.getId().toString(), STREAM_KEY),
                eventJson
        );

        log.info("Saved event to stream with id {}", streamId);
    }

    public Optional<Event> get(UUID key) {
        String json = stringRedisTemplate.opsForValue().get(key.toString());
        if (json == null) return Optional.empty();
        try {
            return Optional.of(objectMapper.readValue(json, Event.class));
        } catch (Exception e) {
            log.error("Error parsing JSON for key {}", key, e);
            throw new RuntimeException(e);
        }
    }

    public void evict(UUID key) {
        stringRedisTemplate.delete(key.toString());
    }
}