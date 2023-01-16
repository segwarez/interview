package com.segwarez.hexagonal.application.rest;

import com.segwarez.hexagonal.application.request.AddProductRequest;
import com.segwarez.hexagonal.application.request.CreateOrderRequest;
import com.segwarez.hexagonal.application.response.CreateOrderResponse;
import com.segwarez.hexagonal.domain.service.OrderService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;

import javax.validation.Valid;
import java.util.UUID;

@Validated
@Controller("/orders")
public class OrderController {
    private final OrderService orderService;

    @Inject
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Post()
    public HttpResponse<?> createOrder(@Body @Valid CreateOrderRequest createOrderRequest) {
        final UUID id = orderService.createOrder(createOrderRequest.getProduct());
        return HttpResponse.status(HttpStatus.CREATED).body(new CreateOrderResponse(id));
    }

    @Post()
    void addProduct(@PathVariable final UUID id, @Body final AddProductRequest addProductRequest) {
        orderService.addProduct(id, addProductRequest.getProduct());
    }

    @Delete("/{id}/products")
    void deleteProduct(@PathVariable final UUID id, @QueryValue final UUID productId) {
        orderService.deleteProduct(id, productId);
    }

    @Post("/{id}/complete")
    void completeOrder(@PathVariable final UUID id) {
        orderService.completeOrder(id);
    }
}
