package com.segwarez.modularmonolith.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderShippingAddress {
    String country;
    String city;
    String postalCode;
    String street;
    String buildingNumber;
    String apartmentNumber;
}