package com.segwarez.order.application.dto;

import java.util.List;
import java.util.UUID;

public record OrderDto(
        UUID orderId,
        String status,
        List<OrderItemDto> items
) {}