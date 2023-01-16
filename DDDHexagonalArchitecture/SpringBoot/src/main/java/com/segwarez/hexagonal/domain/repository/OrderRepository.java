package com.segwarez.hexagonal.domain.repository;

import com.segwarez.hexagonal.domain.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Optional<Order> findById(UUID id);

    void save(Order order);
}
