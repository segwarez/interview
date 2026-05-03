package com.segwarez.order.application.command;

import com.segwarez.order.domain.model.product.vo.ProductId;

public record CreateOrderCommand(
        ProductId productId,
        int quantity
) {}