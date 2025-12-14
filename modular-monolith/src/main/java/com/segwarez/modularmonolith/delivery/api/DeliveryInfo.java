package com.segwarez.modularmonolith.delivery.api;

import java.time.Instant;
import java.util.UUID;

public record DeliveryInfo(
        UUID trackingNumber,
        String destinationAddress,
        Instant estimatedDeliveryTime,
        String status
) {}
