package com.segwarez.order.domain.model.product.vo;

import com.segwarez.order.domain.exception.DomainException;

import java.util.Objects;

public final class ProductName {
    private static final int MAX_LENGTH = 120;
    private final String value;

    private ProductName(String value) {
        var normalized = Objects.requireNonNull(value, "Product name cannot be null").trim();
        if (normalized.isBlank()) throw new DomainException("Product name cannot be blank");
        if (normalized.length() > MAX_LENGTH) throw new DomainException("Product name cannot be longer than " + MAX_LENGTH);
        this.value = normalized;
    }

    public static ProductName of(String value) {
        return new ProductName(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductName other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}