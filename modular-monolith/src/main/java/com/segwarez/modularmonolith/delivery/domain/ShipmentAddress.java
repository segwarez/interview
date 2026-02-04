package com.segwarez.modularmonolith.delivery.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShipmentAddress {
    String country;
    String city;
    String postalCode;
    String street;
    String buildingNumber;
    String apartmentNumber;

    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(street).append(" ").append(buildingNumber);

        if (apartmentNumber != null && !apartmentNumber.isBlank()) {
            sb.append("/").append(apartmentNumber);
        }

        sb.append(", ")
                .append(postalCode).append(" ")
                .append(city).append(", ")
                .append(country);

        return sb.toString();
    }
}
