package com.segwarez.order.domain.model.discount.vo;

import com.segwarez.order.domain.exception.DomainException;

public final class DiscountPercentage {
    private final int value;

    private DiscountPercentage(int value) {
        if (value < 0 || value > 100) throw new DomainException("Discount percentage must be between 0 and 100");
        this.value = value;
    }

    public static DiscountPercentage of(int value) {
        return new DiscountPercentage(value);
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return value + "%";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscountPercentage that)) return false;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}