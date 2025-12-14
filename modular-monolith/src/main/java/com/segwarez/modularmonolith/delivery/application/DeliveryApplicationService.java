package com.segwarez.modularmonolith.delivery.application;

import com.segwarez.modularmonolith.delivery.api.DeliveryFacade;
import com.segwarez.modularmonolith.delivery.api.DeliveryInfo;
import com.segwarez.modularmonolith.delivery.domain.Shipment;
import com.segwarez.modularmonolith.delivery.infrastructure.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryApplicationService implements DeliveryFacade {
    private final ShipmentRepository shipmentRepository;

    @Override
    public DeliveryInfo scheduleDelivery(UUID orderId, String destinationAddress) {
        Shipment shipment = new Shipment(orderId, destinationAddress);
        shipmentRepository.save(shipment);
        return new DeliveryInfo(
                shipment.getTrackingNumber(),
                shipment.getDestinationAddress(),
                shipment.getEta(),
                shipment.getStatus().name()
        );
    }
}
