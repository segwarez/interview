package com.segwarez.modularmonolith.order.infrastructure.repository;

import com.segwarez.modularmonolith.order.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderRepository {
    private final Map<UUID, Order> storage = new ConcurrentHashMap<>();

    public void save(Order order) {
        storage.put(order.getId(), order);
    }
}
