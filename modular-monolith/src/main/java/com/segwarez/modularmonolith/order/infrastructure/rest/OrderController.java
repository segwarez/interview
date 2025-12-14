package com.segwarez.modularmonolith.order.infrastructure.rest;

import com.segwarez.modularmonolith.order.api.OrderConfirmation;
import com.segwarez.modularmonolith.order.api.OrderFacade;
import com.segwarez.modularmonolith.order.api.OrderProduct;
import com.segwarez.modularmonolith.order.api.PlaceOrderCommand;
import com.segwarez.modularmonolith.order.infrastructure.rest.dto.PlaceOrderRequest;
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
@RequestMapping("/order")
public class OrderController {
    private final OrderFacade orderFacade;

    @PostMapping
    public ResponseEntity<OrderConfirmation> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        List<OrderProduct> products = request.products().stream()
                .map(p -> new OrderProduct(
                        UUID.fromString(p.productId()),
                        p.unitPrice(),
                        p.quantity()
                ))
                .toList();

        OrderConfirmation confirmation =
                orderFacade.placeOrder(new PlaceOrderCommand(products, request.destinationAddress()));

        return ResponseEntity.ok(confirmation);
    }
}
