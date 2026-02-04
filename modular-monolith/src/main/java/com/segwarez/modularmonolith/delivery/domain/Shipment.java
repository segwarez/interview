package com.segwarez.modularmonolith.delivery.domain;

import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
public class Shipment {
    private final UUID trackingNumber;
    private final UUID orderId;
    private final ShipmentAddress shipmentAddress;
    private final Instant createdAt;
    private final Instant eta;
    private ShipmentStatus status;

    public Shipment(UUID orderId, ShipmentAddress shipmentAddress) {
        this.trackingNumber = UUID.randomUUID();
        this.orderId = orderId;
        this.shipmentAddress = shipmentAddress;
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