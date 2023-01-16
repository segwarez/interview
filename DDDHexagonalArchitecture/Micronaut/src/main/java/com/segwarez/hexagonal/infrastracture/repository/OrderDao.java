package com.segwarez.hexagonal.infrastracture.repository;

import com.segwarez.hexagonal.domain.Order;
import com.segwarez.hexagonal.domain.repository.OrderRepository;
import com.segwarez.hexagonal.infrastracture.repository.entity.OrderEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class OrderDao implements OrderRepository {
    private final MicronautOrderRepository orderRepository;

    @Inject
    public OrderDao(final MicronautOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<Order> findById(final UUID id) {
        Optional<OrderEntity> entity = orderRepository.findById(id);
        if (entity.isPresent()) {
            return Optional.of((entity.get().toOrder()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void save(final Order order) {
        orderRepository.save(new OrderEntity(order));
    }
}
