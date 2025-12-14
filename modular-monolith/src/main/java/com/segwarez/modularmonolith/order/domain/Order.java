package com.segwarez.modularmonolith.order.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Order {
    private final UUID id;
    private final List<OrderItem> items;
    private final String destinationAddress;
    private final Instant createdAt;
    private OrderStatus status;

    public Order(List<OrderItem> items, String destinationAddress) {
        if (items == null || items.isEmpty())
            throw new IllegalArgumentException("Order must contain items");

        this.id = UUID.randomUUID();
        this.items = List.copyOf(items);
        this.destinationAddress = destinationAddress;
        this.createdAt = Instant.now();
        this.status = OrderStatus.NEW;
    }

    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(OrderItem::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void markPaid() {
        if (status != OrderStatus.NEW) throw new IllegalStateException("Order must be NEW to pay");
        status = OrderStatus.PAID;
    }

    public void markShipped() {
        if (status != OrderStatus.PAID) throw new IllegalStateException("Order must be PAID to ship");
        status = OrderStatus.SHIPPED;
    }
}
