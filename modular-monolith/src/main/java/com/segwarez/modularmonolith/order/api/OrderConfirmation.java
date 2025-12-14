package com.segwarez.modularmonolith.order.api;

import com.segwarez.modularmonolith.order.domain.OrderItem;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderConfirmation(
        UUID orderId,
        List<OrderItem> items,
        BigDecimal totalAmount,
        UUID deliveryTrackingNumber,
        Instant estimatedDeliveryTime
) {}
