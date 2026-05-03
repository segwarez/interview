package com.segwarez.order.infrastructure.adapter.in.rest;

import com.segwarez.order.application.command.AddOrderItemCommand;
import com.segwarez.order.application.command.ApplyDiscountCommand;
import com.segwarez.order.application.command.CompleteOrderCommand;
import com.segwarez.order.application.command.CreateOrderCommand;
import com.segwarez.order.application.command.RemoveOrderItemCommand;
import com.segwarez.order.application.port.in.AddOrderItemUseCase;
import com.segwarez.order.application.port.in.ApplyDiscountUseCase;
import com.segwarez.order.application.port.in.CompleteOrderUseCase;
import com.segwarez.order.application.port.in.CreateOrderUseCase;
import com.segwarez.order.application.port.in.GetOrderUseCase;
import com.segwarez.order.application.port.in.RemoveOrderItemUseCase;
import com.segwarez.order.application.query.GetOrderQuery;
import com.segwarez.order.application.dto.OrderDto;
import com.segwarez.order.domain.model.discount.vo.DiscountCode;
import com.segwarez.order.domain.model.order.vo.OrderId;
import com.segwarez.order.domain.model.product.vo.ProductId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final AddOrderItemUseCase addOrderItemUseCase;
    private final RemoveOrderItemUseCase removeOrderItemUseCase;
    private final ApplyDiscountUseCase applyDiscountUseCase;
    private final CompleteOrderUseCase completeOrderUseCase;

    OrderController(CreateOrderUseCase createOrderUseCase,
                    GetOrderUseCase getOrderUseCase,
                    AddOrderItemUseCase addOrderItemUseCase,
                    RemoveOrderItemUseCase removeOrderItemUseCase,
                    ApplyDiscountUseCase applyDiscountUseCase,
                    CompleteOrderUseCase completeOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.addOrderItemUseCase = addOrderItemUseCase;
        this.removeOrderItemUseCase = removeOrderItemUseCase;
        this.applyDiscountUseCase = applyDiscountUseCase;
        this.completeOrderUseCase = completeOrderUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(createOrderUseCase.createOrder(
                new CreateOrderCommand(
                        ProductId.of(request.productId()),
                        request.quantity()
                )
        ));
    }

    @GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDto> getOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(getOrderUseCase.getOrder(
                new GetOrderQuery(OrderId.of(orderId)
                )
        ));
    }

    @PatchMapping(value = "/{orderId}/items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDto> addItem(@PathVariable UUID orderId,
                     @RequestBody AddOrderItemRequest request) {
        return ResponseEntity.ok(addOrderItemUseCase.addItem(
                new AddOrderItemCommand(
                        OrderId.of(orderId),
                        ProductId.of(request.productId()),
                        request.quantity()
                )
        ));
    }

    @DeleteMapping(value = "/{orderId}/items/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDto> removeItem(@PathVariable UUID orderId,
                                @PathVariable UUID productId,
                                @RequestParam int quantity) {
        return ResponseEntity.ok(removeOrderItemUseCase.removeItem(
                new RemoveOrderItemCommand(
                        OrderId.of(orderId),
                        ProductId.of(productId),
                        quantity
                )
        ));
    }

    @PatchMapping(value = "/{orderId}/discount", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDto> applyDiscount(@PathVariable UUID orderId,
                           @RequestBody ApplyDiscountRequest request) {
        return ResponseEntity.ok(applyDiscountUseCase.applyDiscount(
                new ApplyDiscountCommand(
                        OrderId.of(orderId),
                        DiscountCode.of(request.discountCode())
                )
        ));
    }

    @PostMapping(value = "/{orderId}/complete", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDto> complete(@PathVariable UUID orderId) {
        return ResponseEntity.ok(completeOrderUseCase.completeOrder(
                new CompleteOrderCommand(OrderId.of(orderId))
        ));
    }
}