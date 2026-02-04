package com.segwarez.modularmonolith.order.api;

import com.segwarez.modularmonolith.order.domain.OrderItem;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
public class OrderConfirmation {
    UUID orderId;
    List<OrderItem> items;
    BigDecimal totalAmount;
    UUID deliveryTrackingNumber;
    Instant estimatedDeliveryTime;
}