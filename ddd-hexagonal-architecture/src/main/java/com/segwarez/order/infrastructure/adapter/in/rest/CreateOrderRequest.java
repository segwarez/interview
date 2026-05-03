package com.segwarez.order.infrastructure.adapter.in.rest;

import java.util.UUID;

record CreateOrderRequest(
        UUID productId,
        int quantity
) {}