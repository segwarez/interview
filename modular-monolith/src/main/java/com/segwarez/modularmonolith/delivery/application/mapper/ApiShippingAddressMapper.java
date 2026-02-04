package com.segwarez.modularmonolith.delivery.application.mapper;

import com.segwarez.modularmonolith.delivery.api.ShippingAddress;
import com.segwarez.modularmonolith.delivery.domain.ShipmentAddress;

public class ApiShippingAddressMapper {
    private ApiShippingAddressMapper() {}
    public static ShippingAddress toApi(ShipmentAddress shipmentAddress) {
        return new ShippingAddress(
                shipmentAddress.getCountry(),
                shipmentAddress.getCity(),
                shipmentAddress.getPostalCode(),
                shipmentAddress.getStreet(),
                shipmentAddress.getBuildingNumber(),
                shipmentAddress.getApartmentNumber()
        );
    }
}
