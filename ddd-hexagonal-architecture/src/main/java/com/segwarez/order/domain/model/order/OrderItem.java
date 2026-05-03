package com.segwarez.order.domain.model.order;

import com.segwarez.order.domain.exception.DomainException;
import com.segwarez.order.domain.model.discount.Discount;
import com.segwarez.order.domain.model.pricing.vo.Price;
import com.segwarez.order.domain.model.product.vo.ProductId;
import com.segwarez.order.domain.model.product.vo.ProductName;
import com.segwarez.order.domain.model.product.vo.ProductSnapshot;

import java.util.Objects;

public final class OrderItem {
    private final ProductSnapshot product;
    private final Discount discount;
    private final int quantity;

    private OrderItem(ProductSnapshot product, Discount discount, int quantity) {
        this.product = Objects.requireNonNull(product, "Product snapshot cannot be null");
        this.discount = Objects.requireNonNull(discount, "Discount cannot be null");

        if (quantity <= 0) {
            throw new DomainException("Order item quantity must be greater than 0");
        }

        this.quantity = quantity;
    }

    public static OrderItem of(ProductSnapshot product, int quantity) {
        return new OrderItem(product, Discount.none(), quantity);
    }

    public static OrderItem of(ProductSnapshot product, Discount discount, int quantity) {
        return new OrderItem(product, discount, quantity);
    }

    public OrderItem increaseQuantityBy(int additionalQuantity) {
        if (additionalQuantity <= 0) {
            throw new DomainException("Additional quantity must be greater than 0");
        }
        return new OrderItem(product, discount, this.quantity + additionalQuantity);
    }

    public OrderItem decreaseQuantityBy(int quantityToRemove) {
        if (quantityToRemove <= 0) {
            throw new DomainException("Quantity to remove must be greater than 0");
        }

        if (quantityToRemove >= this.quantity) {
            throw new DomainException("Quantity to remove must be lower than current quantity");
        }

        return new OrderItem(product, discount, this.quantity - quantityToRemove);
    }

    public OrderItem applyDiscount(Discount discount) {
        Objects.requireNonNull(discount, "Discount cannot be null");
        return new OrderItem(product, discount, quantity);
    }

    public ProductId productId() {
        return product.productId();
    }

    public ProductName productName() {
        return product.productName();
    }

    public int quantity() {
        return quantity;
    }

    public Price unitPrice() {
        return product.unitPrice();
    }

    public Discount discount() {
        return discount;
    }

    public Price grossTotalPrice() {
        return product.unitPrice().multiply(quantity);
    }

    public Price discountAmount() {
        return discount.calculateFor(grossTotalPrice());
    }

    public Price totalPrice() {
        return grossTotalPrice().subtract(discountAmount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem other)) return false;
        return product.equals(other.product) && discount.equals(other.discount) && quantity == other.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, discount, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId=" + productId() +
                ", productName=" + product.productName().value() +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice() +
                ", gross=" + grossTotalPrice() +
                ", discount=" + discount +
                ", total=" + totalPrice() +
                '}';
    }
}