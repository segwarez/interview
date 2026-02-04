package com.segwarez.modularmonolith.order.infrastructure.rest.dto;

import com.segwarez.modularmonolith.order.api.ShippingAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

@Value
public class ShippingAddressDTO {
    @NotBlank
    String country;
    @NotBlank
    String city;
    @NotBlank
    @Pattern(regexp = "\\d{2}-\\d{3}")
    String postalCode;
    @NotBlank
    String street;
    @NotBlank
    String buildingNumber;
    String apartmentNumber;

    public ShippingAddress toApi() {
        return new ShippingAddress(
                country,
                city,
                postalCode,
                street,
                buildingNumber,
                apartmentNumber
        );
    }
}
