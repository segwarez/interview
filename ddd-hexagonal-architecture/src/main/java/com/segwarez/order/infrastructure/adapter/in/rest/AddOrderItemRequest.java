package com.segwarez.order.infrastructure.adapter.in.rest;

import java.util.UUID;

record AddOrderItemRequest(
        UUID productId,
        int quantity
) {}