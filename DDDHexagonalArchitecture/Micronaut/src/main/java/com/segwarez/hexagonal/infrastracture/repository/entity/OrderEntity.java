package com.segwarez.hexagonal.infrastracture.repository.entity;

import com.segwarez.hexagonal.domain.Order;

import com.segwarez.hexagonal.domain.OrderItem;
import com.segwarez.hexagonal.domain.OrderStatus;
import com.segwarez.hexagonal.domain.Product;
import io.micronaut.core.annotation.Introspected;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Introspected
public class OrderEntity {
    @Id
    private UUID id;

    private OrderStatus status;

    @OneToMany
    private List<OrderItemEntity> orderItemEntities;

    private BigDecimal price;

    public OrderEntity() {
    }

    public OrderEntity(Order order) {
        this.id = order.getId();
        this.price = order.getPrice();
        this.status = order.getStatus();
        this.orderItemEntities = order.getOrderItems()
                .stream()
                .map(OrderItemEntity::new)
                .collect(Collectors.toList());
    }

    public Order toOrder() {
        List<OrderItem> orderItems = orderItemEntities.stream()
                .map(OrderItemEntity::toOrderItem)
                .collect(Collectors.toList());
        List<Product> namelessProducts = orderItems.stream()
                .map(orderItem -> new Product(orderItem.getProductId(), orderItem.getPrice(), ""))
                .collect(Collectors.toList());
        Order order = new Order(id, namelessProducts.remove(0));
        namelessProducts.forEach(product -> order.addOrder(product));
        if (status == OrderStatus.COMPLETED) {
            order.complete();
        }
        return order;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemEntity> getOrderItemEntities() {
        return orderItemEntities;
    }

    public void setOrderItemEntities(List<OrderItemEntity> orderItemEntities) {
        this.orderItemEntities = orderItemEntities;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
