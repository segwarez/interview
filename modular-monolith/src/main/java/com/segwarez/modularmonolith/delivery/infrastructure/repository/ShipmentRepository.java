package com.segwarez.modularmonolith.delivery.infrastructure.repository;

import com.segwarez.modularmonolith.delivery.domain.Shipment;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ShipmentRepository {
    private final Map<UUID, Shipment> shipments = new ConcurrentHashMap<>();

    public void save(Shipment shipment) {
        shipments.put(shipment.getTrackingNumber(), shipment);
    }
}
