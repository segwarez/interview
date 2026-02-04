package com.segwarez.modularmonolith.order.infrastructure.rest.request;

import com.segwarez.modularmonolith.order.infrastructure.rest.dto.PaymentMethodDTO;
import com.segwarez.modularmonolith.order.infrastructure.rest.dto.ProductDTO;
import com.segwarez.modularmonolith.order.infrastructure.rest.dto.ShippingAddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class PlaceOrderRequest {
    @NotEmpty
    @Valid
    List<ProductDTO> products;
    @NotNull
    ShippingAddressDTO shippingAddress;
    @NotNull
    PaymentMethodDTO paymentMethod;
}
