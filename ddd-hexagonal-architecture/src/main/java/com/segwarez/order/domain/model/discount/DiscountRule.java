package com.segwarez.order.domain.model.discount;

import com.segwarez.order.domain.model.discount.vo.DiscountCode;
import com.segwarez.order.domain.model.discount.vo.EffectiveDates;
import com.segwarez.order.domain.model.order.Order;

import java.time.LocalDate;
import java.util.Objects;

public abstract class DiscountRule {
    protected final Discount discount;
    protected final EffectiveDates effectiveDates;

    protected DiscountRule(Discount discount, EffectiveDates effectiveDates) {
        this.discount = Objects.requireNonNull(discount, "Discount cannot be null");
        this.effectiveDates = Objects.requireNonNull(effectiveDates, "Effective dates cannot be null");
    }

    public final boolean apply(Order order, DiscountCode discountCode, LocalDate evaluationDate) {
        Objects.requireNonNull(order, "Order cannot be null");
        Objects.requireNonNull(discountCode, "Discount code cannot be null");
        Objects.requireNonNull(evaluationDate, "Discount evaluation date cannot be null.");
        if (!discount.code().equals(discountCode)) return false;
        if (!effectiveDates.includes(evaluationDate)) return false;
        return applyRule(order, discountCode);
    }

    protected abstract boolean applyRule(Order order, DiscountCode discountCode);
}