package com.segwarez.modularmonolith.delivery.domain;

import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
public class Shipment {
    private final UUID trackingNumber;
    private final UUID orderId;
    private final String destinationAddress;
    private final Instant createdAt;
    private final Instant eta;
    private ShipmentStatus status;

    public Shipment(UUID orderId, String destinationAddress) {
        this.trackingNumber = UUID.randomUUID();
        this.orderId = orderId;
        this.destinationAddress = destinationAddress;
        this.createdAt = Instant.now();
        this.eta = createdAt.plus(2, ChronoUnit.DAYS);
        this.status = ShipmentStatus.CREATED;
    }

    public void markInTransit() {
        status = ShipmentStatus.IN_TRANSIT;
    }

    public void markDelivered() {
        status = ShipmentStatus.DELIVERED;
    }
}