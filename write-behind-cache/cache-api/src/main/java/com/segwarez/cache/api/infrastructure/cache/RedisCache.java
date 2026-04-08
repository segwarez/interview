package com.segwarez.cache.api.infrastructure.cache;

import com.segwarez.cache.api.domain.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCache {
    private static final String STREAM_KEY = "WRITE_BEHIND_STREAM";

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public void save(Event event) {
        stringRedisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) {
                try {
                    operations.multi();
                    var jsonEvent = objectMapper.writeValueAsString(event);
                    operations.opsForValue().set(event.getId().toString(), jsonEvent);
                    Map<String, String> streamEntry = Map.of(
                            "id", event.getId().toString(),
                            "type", event.getType(),
                            "userId", event.getUserId(),
                            "timestamp", event.getTimestamp().toString(),
                            "payload", event.getPayload()
                    );
                    operations.opsForStream().add(STREAM_KEY, streamEntry);
                    return operations.exec();
                } catch (Exception e) {
                    log.error("Error saving event to Redis", e);
                    operations.discard();
                    return null;
                }
            }
        });
    }

    public Optional<Event> get(UUID key) {
        String json = stringRedisTemplate.opsForValue().get(key.toString());
        if (json == null) return Optional.empty();
        try {
            return Optional.of(objectMapper.readValue(json, Event.class));
        } catch (JacksonException e) {
            log.error("Error parsing JSON for key {}", key, e);
            throw e;
        }
    }

    public void evict(UUID key) {
        stringRedisTemplate.delete(key.toString());
    }
}