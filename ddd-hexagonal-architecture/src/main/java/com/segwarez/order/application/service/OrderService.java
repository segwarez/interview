package com.segwarez.order.application.service;

import com.segwarez.order.application.command.AddOrderItemCommand;
import com.segwarez.order.application.command.ApplyDiscountCommand;
import com.segwarez.order.application.command.CompleteOrderCommand;
import com.segwarez.order.application.command.CreateOrderCommand;
import com.segwarez.order.application.command.RemoveOrderItemCommand;
import com.segwarez.order.application.exception.OrderNotFoundException;
import com.segwarez.order.application.exception.ProductNotFoundException;
import com.segwarez.order.application.mapper.OrderDtoMapper;
import com.segwarez.order.application.port.in.AddOrderItemUseCase;
import com.segwarez.order.application.port.in.ApplyDiscountUseCase;
import com.segwarez.order.application.port.in.CompleteOrderUseCase;
import com.segwarez.order.application.port.in.CreateOrderUseCase;
import com.segwarez.order.application.port.in.GetOrderUseCase;
import com.segwarez.order.application.port.in.RemoveOrderItemUseCase;
import com.segwarez.order.application.port.out.OrderRepository;
import com.segwarez.order.application.port.out.ProductRepository;
import com.segwarez.order.application.query.GetOrderQuery;
import com.segwarez.order.application.dto.OrderDto;
import com.segwarez.order.domain.model.discount.DiscountEvaluator;
import com.segwarez.order.domain.model.order.Order;
import com.segwarez.order.domain.model.order.vo.OrderId;

import java.util.Objects;

public class OrderService implements
        CreateOrderUseCase,
        GetOrderUseCase,
        AddOrderItemUseCase,
        RemoveOrderItemUseCase,
        ApplyDiscountUseCase,
        CompleteOrderUseCase {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DiscountEvaluator discountEvaluator;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            DiscountEvaluator discountEvaluator) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.discountEvaluator = discountEvaluator;
    }

    @Override
    public OrderDto createOrder(CreateOrderCommand command) {
        Objects.requireNonNull(command, "Create order command cannot be null");
        var product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));
        var order = Order.create(product, command.quantity());
        var savedOrder = orderRepository.save(order);
        return OrderDtoMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto getOrder(GetOrderQuery query) {
        Objects.requireNonNull(query, "Get order query cannot be null");
        var order = getOrder(query.orderId());
        return OrderDtoMapper.toDto(order);
    }

    @Override
    public OrderDto addItem(AddOrderItemCommand command) {
        Objects.requireNonNull(command, "Add order item command cannot be null");
        var order = getOrder(command.orderId());
        var product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));
        order.addItem(product, command.quantity());
        var savedOrder = orderRepository.save(order);
        return OrderDtoMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto removeItem(RemoveOrderItemCommand command) {
        Objects.requireNonNull(command, "Remove order item command cannot be null");
        var order = getOrder(command.orderId());
        order.removeItem(command.productId(), command.quantity());
        var savedOrder = orderRepository.save(order);
        return OrderDtoMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto applyDiscount(ApplyDiscountCommand command) {
        Objects.requireNonNull(command, "Apply discount command cannot be null");
        var order = getOrder(command.orderId());
        discountEvaluator.applyDiscount(order, command.discountCode());
        var savedOrder = orderRepository.save(order);
        return OrderDtoMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto completeOrder(CompleteOrderCommand command) {
        Objects.requireNonNull(command, "Complete order command cannot be null");
        var order = getOrder(command.orderId());
        order.complete();
        var savedOrder = orderRepository.save(order);
        return OrderDtoMapper.toDto(savedOrder);
    }

    private Order getOrder(OrderId orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}