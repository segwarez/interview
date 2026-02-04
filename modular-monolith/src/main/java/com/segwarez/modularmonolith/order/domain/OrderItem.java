package com.segwarez.modularmonolith.order.domain;

import com.segwarez.modularmonolith.order.api.OrderProduct;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OrderItem {
    private final UUID productId;
    private final BigDecimal unitPrice;
    private final int quantity;

    public OrderItem(UUID productId, BigDecimal unitPrice, int quantity) {
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public OrderItem(OrderProduct p) {
        this(p.getProductId(), p.getUnitPrice(), p.getQuantity());
    }

    public BigDecimal getTotalAmount() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}