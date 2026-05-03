package com.segwarez.order.infrastructure.adapter.in.messaging;

public record CreateOrderMessage(
        String productId,
        int quantity
) {}