package com.segwarez.order.domain.model.product.vo;

import com.segwarez.order.domain.exception.DomainException;

import java.util.Objects;
import java.util.UUID;

public final class ProductId {
    private final UUID value;

    private ProductId(UUID value) {
        this.value = Objects.requireNonNull(value, "Product ID cannot be null");
    }

    public static ProductId of(UUID value) {
        return new ProductId(value);
    }

    public static ProductId of(String value) {
        Objects.requireNonNull(value, "Product ID cannot be null");
        try {
            return new ProductId(UUID.fromString(value));
        } catch (IllegalArgumentException ex) {
            throw new DomainException("Invalid Product ID: " + value);
        }
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
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
        if (!(o instanceof ProductId other)) return false;
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