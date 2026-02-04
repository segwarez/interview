package com.segwarez.modularmonolith.delivery.api;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class DeliveryInfo {
    UUID trackingNumber;
    ShippingAddress shippingAddress;
    Instant estimatedDeliveryTime;
    String status;
}
