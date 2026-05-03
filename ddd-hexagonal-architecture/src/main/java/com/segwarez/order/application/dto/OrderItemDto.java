package com.segwarez.order.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDto(
        UUID productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        String currency,
        String discountCode,
        int discountPercentage
) {}