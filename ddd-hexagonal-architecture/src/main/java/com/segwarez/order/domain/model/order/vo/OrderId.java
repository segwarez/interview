package com.segwarez.order.domain.model.order.vo;

import com.segwarez.order.domain.exception.DomainException;

import java.util.Objects;
import java.util.UUID;

public final class OrderId {
    private final UUID value;

    private OrderId(UUID value) {
        this.value = Objects.requireNonNull(value, "Order ID cannot be null");
    }

    public static OrderId of(UUID value) {
        return new OrderId(value);
    }

    public static OrderId of(String value) {
        Objects.requireNonNull(value, "Order ID cannot be null");
        try {
            return new OrderId(UUID.fromString(value));
        } catch (IllegalArgumentException ex) {
            throw new DomainException("Invalid Order ID: " + value);
        }
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    public String asString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderId other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}