package com.segwarez.modularmonolith.delivery.application;

import com.segwarez.modularmonolith.delivery.api.DeliveryFacade;
import com.segwarez.modularmonolith.delivery.api.DeliveryInfo;
import com.segwarez.modularmonolith.delivery.api.ShippingAddress;
import com.segwarez.modularmonolith.delivery.domain.Shipment;
import com.segwarez.modularmonolith.delivery.infrastructure.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.segwarez.modularmonolith.delivery.application.mapper.ApiShippingAddressMapper.toApi;

@Service
@RequiredArgsConstructor
public class DeliveryApplicationService implements DeliveryFacade {
    private final ShipmentRepository shipmentRepository;

    @Override
    public DeliveryInfo scheduleDelivery(UUID orderId, ShippingAddress shippingAddress) {
        Shipment shipment = new Shipment(orderId, shippingAddress.toDomain());
        shipmentRepository.save(shipment);
        return new DeliveryInfo(
                shipment.getTrackingNumber(),
                toApi(shipment.getShipmentAddress()),
                shipment.getEta(),
                shipment.getStatus().name()
        );
    }
}
