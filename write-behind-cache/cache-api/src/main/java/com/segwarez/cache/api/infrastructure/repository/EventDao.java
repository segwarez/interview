package com.segwarez.cache.api.infrastructure.repository;

import com.segwarez.cache.api.domain.Event;
import com.segwarez.cache.api.infrastructure.cache.RedisCache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class EventDao {
    private final JPAEventRepository jpaEventRepository;
    private final RedisCache redisCache;

    public void save(Event event) {
        redisCache.save(event);
    }

    public Optional<Event> get(UUID id) {
        var fromCache = redisCache.get(id);

        if (fromCache.isPresent()) {
            return fromCache;
        }

        return jpaEventRepository.findById(id)
                .map(entity -> {
                    Event event = entity.toDomain();
                    redisCache.save(event);
                    return event;
                });
    }

    public void delete(UUID id) {
        jpaEventRepository.deleteById(id);
        redisCache.evict(id);
    }
}