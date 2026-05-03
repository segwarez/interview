package com.segwarez.order.application.command;

import com.segwarez.order.domain.model.order.vo.OrderId;
import com.segwarez.order.domain.model.product.vo.ProductId;

public record AddOrderItemCommand(
        OrderId orderId,
        ProductId productId,
        int quantity
) {}