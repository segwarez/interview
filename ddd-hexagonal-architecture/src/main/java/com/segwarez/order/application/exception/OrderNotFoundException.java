package com.segwarez.order.application.exception;

import com.segwarez.order.domain.model.order.vo.OrderId;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(OrderId orderId) {
        super("Order not found: " + orderId);
    }
}