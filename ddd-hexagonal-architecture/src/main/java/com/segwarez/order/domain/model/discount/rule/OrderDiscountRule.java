package com.segwarez.order.domain.model.discount.rule;

import com.segwarez.order.domain.model.discount.Discount;
import com.segwarez.order.domain.model.discount.DiscountRule;
import com.segwarez.order.domain.model.discount.vo.DiscountCode;
import com.segwarez.order.domain.model.discount.vo.DiscountPercentage;
import com.segwarez.order.domain.model.discount.vo.EffectiveDates;
import com.segwarez.order.domain.model.order.Order;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderDiscountRule extends DiscountRule {
    private static final BigDecimal MIN_ORDER_VALUE = BigDecimal.valueOf(300);

    public OrderDiscountRule() {
        super(Discount.of(
                DiscountCode.of("ORDER300"),
                DiscountPercentage.of(10)
                ),
                EffectiveDates.between(
                        LocalDate.of(2026, 4, 1),
                        LocalDate.of(2026, 7, 1))
        );
    }

    @Override
    protected boolean applyRule(Order order, DiscountCode discountCode) {
        var grossTotalPrice = order.grossTotalPrice();
        if (grossTotalPrice.amount().compareTo(MIN_ORDER_VALUE) <= 0) return false;
        order.items().forEach(item -> order.applyDiscount(item.productId(), discount));
        return true;
    }
}