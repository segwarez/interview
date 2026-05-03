package com.segwarez.order.domain.model.pricing.vo;

import com.segwarez.order.domain.exception.DomainException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public final class Price {
    private static final int SCALE = 2;
    private final BigDecimal amount;
    private final Currency currency;

    private Price(BigDecimal amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null").setScale(SCALE, RoundingMode.HALF_UP);
        if (this.amount.compareTo(BigDecimal.ZERO) < 0) throw new DomainException("Price cannot be negative");
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
    }

    public static Price of(BigDecimal amount, Currency currency) {
        return new Price(amount, currency);
    }

    public static Price zero(Currency currency) {
        return new Price(BigDecimal.ZERO, currency);
    }

    public BigDecimal amount() {
        return amount;
    }

    public Currency currency() {
        return currency;
    }

    public Price add(Price other) {
        Objects.requireNonNull(other, "Price cannot be null");
        assertSameCurrency(other);
        return new Price(this.amount.add(other.amount), currency);
    }

    public Price subtract(Price other) {
        Objects.requireNonNull(other, "Price cannot be null");
        assertSameCurrency(other);
        if (this.amount.compareTo(other.amount) < 0) throw new DomainException("Resulting price cannot be negative");
        return new Price(this.amount.subtract(other.amount), currency);
    }

    public Price multiply(int quantity) {
        if (quantity < 0) throw new DomainException("Quantity cannot be negative");
        return new Price(this.amount.multiply(BigDecimal.valueOf(quantity)), currency);
    }

    public Price percentage(int percentage) {
        if (percentage < 0 || percentage > 100) throw new DomainException("Percentage must be between 0 and 100");
        var result = this.amount.multiply(BigDecimal.valueOf(percentage)).divide(BigDecimal.valueOf(100), SCALE, RoundingMode.HALF_UP);
        return new Price(result, currency);
    }

    private void assertSameCurrency(Price other) {
        if (!this.currency.equals(other.currency)) throw new DomainException("Currency mismatch");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price other)) return false;
        return amount.equals(other.amount) && currency.equals(other.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency.getCurrencyCode();
    }
}