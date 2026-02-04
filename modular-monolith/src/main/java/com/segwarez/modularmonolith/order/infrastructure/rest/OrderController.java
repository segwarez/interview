package com.segwarez.modularmonolith.order.infrastructure.rest;

import com.segwarez.modularmonolith.order.api.*;
import com.segwarez.modularmonolith.order.infrastructure.rest.request.PlaceOrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderFacade orderFacade;

    @PostMapping
    public ResponseEntity<OrderConfirmation> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        List<OrderProduct> products = request.getProducts().stream()
                .map(p -> new OrderProduct(
                        UUID.fromString(p.getProductId()),
                        p.getUnitPrice(),
                        p.getQuantity()
                ))
                .toList();

        OrderConfirmation confirmation =
                orderFacade.placeOrder(new PlaceOrderCommand(
                        products,
                        request.getShippingAddress().toApi(),
                        request.getPaymentMethod().toApi()));

        return ResponseEntity.ok(confirmation);
    }
}
