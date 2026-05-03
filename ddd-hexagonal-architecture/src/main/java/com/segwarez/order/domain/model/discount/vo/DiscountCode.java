package com.segwarez.order.domain.model.discount.vo;

import com.segwarez.order.domain.exception.DomainException;

import java.util.Objects;

public final class DiscountCode {
    private static final String PATTERN = "^[A-Z0-9]{8}$";

    private final String value;

    private DiscountCode(String value) {
        String normalized = Objects.requireNonNull(value, "Discount code cannot be null")
                .trim()
                .toUpperCase();
        if (!normalized.matches(PATTERN)) throw new DomainException("Discount code must be exactly 8 characters (A-Z, 0-9)");
        this.value = normalized;
    }

    public static DiscountCode of(String value) {
        return new DiscountCode(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscountCode other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}