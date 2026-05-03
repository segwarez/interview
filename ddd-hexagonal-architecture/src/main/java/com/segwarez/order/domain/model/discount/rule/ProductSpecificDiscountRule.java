package com.segwarez.order.domain.model.discount.rule;

import com.segwarez.order.domain.model.discount.Discount;
import com.segwarez.order.domain.model.discount.DiscountRule;
import com.segwarez.order.domain.model.discount.vo.DiscountPercentage;
import com.segwarez.order.domain.model.discount.vo.DiscountCode;
import com.segwarez.order.domain.model.discount.vo.EffectiveDates;
import com.segwarez.order.domain.model.order.Order;
import com.segwarez.order.domain.model.product.vo.ProductId;

import java.time.LocalDate;

public class ProductSpecificDiscountRule extends DiscountRule {
    private static final ProductId PRODUCT_ID = ProductId.of("11111111-1111-1111-1111-111111111111");

    public ProductSpecificDiscountRule() {
        super(Discount.of(
                DiscountCode.of("IPHONE25"),
                DiscountPercentage.of(25)
                ),
                EffectiveDates.between(
                        LocalDate.of(2026, 4, 1),
                        LocalDate.of(2026, 7, 1))
        );
    }

    @Override
    protected boolean applyRule(Order order, DiscountCode discountCode) {
        for (var item : order.items()) {
            if (item.productId().equals(PRODUCT_ID)) {
                order.applyDiscount(item.productId(), discount);
                return true;
            }
        }
        return false;
    }
}