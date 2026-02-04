package com.segwarez.modularmonolith.delivery.api;

import com.segwarez.modularmonolith.delivery.domain.ShipmentAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShippingAddress {
    String country;
    String city;
    String postalCode;
    String street;
    String buildingNumber;
    String apartmentNumber;

    public ShipmentAddress toDomain() {
        return new ShipmentAddress(
                country,
                city,
                postalCode,
                street,
                buildingNumber,
                apartmentNumber
        );
    }
}