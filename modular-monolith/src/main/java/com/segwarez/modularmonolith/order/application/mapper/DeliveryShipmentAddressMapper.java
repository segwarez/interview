package com.segwarez.modularmonolith.order.application.mapper;

import com.segwarez.modularmonolith.delivery.api.ShippingAddress;
import com.segwarez.modularmonolith.order.domain.OrderShippingAddress;

public class DeliveryShipmentAddressMapper {
    private DeliveryShipmentAddressMapper() {}
    public static ShippingAddress toDelivery(OrderShippingAddress orderShippingAddress) {
        return new ShippingAddress(
                orderShippingAddress.getCountry(),
                orderShippingAddress.getCity(),
                orderShippingAddress.getPostalCode(),
                orderShippingAddress.getStreet(),
                orderShippingAddress.getBuildingNumber(),
                orderShippingAddress.getApartmentNumber()
        );
    }
}
