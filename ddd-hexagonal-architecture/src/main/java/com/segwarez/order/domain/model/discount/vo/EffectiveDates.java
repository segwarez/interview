package com.segwarez.order.domain.model.discount.vo;

import com.segwarez.order.domain.exception.DomainException;

import java.time.LocalDate;
import java.util.Objects;

public final class EffectiveDates {
    private final LocalDate from;
    private final LocalDate to;

    private EffectiveDates(LocalDate from, LocalDate to) {
        this.from = Objects.requireNonNull(from, "Effective from date cannot be null");
        this.to = Objects.requireNonNull(to, "Effective to date cannot be null");

        if (to.isBefore(from)) {
            throw new DomainException("Effective to date cannot be before from date");
        }
    }

    public static EffectiveDates between(LocalDate from, LocalDate to) {
        return new EffectiveDates(from, to);
    }

    public boolean includes(LocalDate date) {
        Objects.requireNonNull(date, "Date cannot be null");
        return !date.isBefore(from) && !date.isAfter(to);
    }

    @Override
    public String toString() {
        return "EffectiveDates{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EffectiveDates that)) return false;
        return from.equals(that.from) && to.equals(that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}