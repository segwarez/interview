package com.segwarez.hexagonal.infrastracture.repository.entity;

import com.segwarez.hexagonal.domain.OrderItem;
import com.segwarez.hexagonal.domain.Product;
import io.micronaut.core.annotation.Introspected;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Introspected
public class OrderItemEntity {
    @Id
    private UUID productId;

    private BigDecimal price;

    private String name;

    public OrderItemEntity() {
    }

    public OrderItemEntity(final OrderItem orderItem) {
        this.productId = orderItem.getProductId();
        this.price = orderItem.getPrice();
    }

    public OrderItem toOrderItem() {
        return new OrderItem(new Product(productId, price, ""));
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID id) {
        this.productId = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }
}
