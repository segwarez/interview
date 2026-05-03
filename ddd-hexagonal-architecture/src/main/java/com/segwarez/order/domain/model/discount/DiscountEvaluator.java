package com.segwarez.order.domain.model.discount;

import com.segwarez.order.domain.model.discount.vo.DiscountCode;
import com.segwarez.order.domain.model.order.Order;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DiscountEvaluator {
    private final List<DiscountRule> rules;
    private final Clock clock;

    public DiscountEvaluator(List<DiscountRule> rules, Clock clock) {
        this.rules = List.copyOf(Objects.requireNonNull(rules, "Discount rules cannot be null."));
        this.clock = Objects.requireNonNull(clock, "Clock cannot be null.");
    }

    public void applyDiscount(Order order, DiscountCode discountCode) {
        Objects.requireNonNull(order, "Order cannot be null.");
        Objects.requireNonNull(discountCode, "Discount code cannot be null.");

        for (DiscountRule rule : rules) {
            if (rule.apply(order, discountCode, LocalDate.now(clock))) return;
        }
    }
}