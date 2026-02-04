package com.segwarez.modularmonolith.order.api;

import com.segwarez.modularmonolith.order.domain.OrderShippingAddress;
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

    public OrderShippingAddress toDomain() {
        return new OrderShippingAddress(
                country,
                city,
                postalCode,
                street,
                buildingNumber,
                apartmentNumber
        );
    }
}