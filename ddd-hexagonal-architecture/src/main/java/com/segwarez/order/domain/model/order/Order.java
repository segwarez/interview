package com.segwarez.order.domain.model.order;

import com.segwarez.order.domain.exception.DomainException;
import com.segwarez.order.domain.model.discount.Discount;
import com.segwarez.order.domain.model.order.vo.OrderId;
import com.segwarez.order.domain.model.pricing.vo.Price;
import com.segwarez.order.domain.model.product.Product;
import com.segwarez.order.domain.model.product.vo.ProductId;

import java.util.*;

public class Order {
    private final OrderId id;
    private OrderStatus status;
    private final Map<ProductId, OrderItem> orderItems;

    private Order(OrderId id, OrderStatus status, Map<ProductId, OrderItem> orderItems) {
        this.id = Objects.requireNonNull(id, "Order ID cannot be null");
        this.status = Objects.requireNonNull(status, "Order status cannot be null");
        if (orderItems.isEmpty()) throw new DomainException("Order must contain at least one item");
        this.orderItems = new LinkedHashMap<>(orderItems);

        validateCurrencyConsistency();
    }

    public static Order create(Product product, int quantity) {
        Objects.requireNonNull(product, "Product cannot be null");
        var item = OrderItem.of(product.snapshot(), quantity);
        return new Order(
                OrderId.generate(),
                OrderStatus.CREATED,
                Map.of(item.productId(), item)
        );
    }

    public static Order restore(OrderId id, OrderStatus status, List<OrderItem> orderItems) {
        Objects.requireNonNull(orderItems, "Order items cannot be null");
        Map<ProductId, OrderItem> itemsByProductId = new LinkedHashMap<>();

        for (OrderItem item : orderItems) {
            if (itemsByProductId.put(item.productId(), item) != null) {
                throw new DomainException("Duplicated product in order: " + item.productId());
            }
        }

        return new Order(id, status, itemsByProductId);
    }

    public void complete() {
        validateNotCompleted();
        this.status = OrderStatus.COMPLETED;
    }

    public void addItem(Product product, int quantity) {
        Objects.requireNonNull(product, "Product cannot be null");
        validateNotCompleted();

        OrderItem newItem = OrderItem.of(product.snapshot(), quantity);
        validateSameCurrency(newItem);

        orderItems.merge(
                newItem.productId(),
                newItem,
                (existing, added) -> existing.increaseQuantityBy(quantity)
        );
    }

    public void removeItem(ProductId productId, int quantity) {
        validateNotCompleted();
        Objects.requireNonNull(productId, "Product ID cannot be null");

        if (quantity <= 0) {
            throw new DomainException("Quantity to remove must be greater than 0");
        }

        OrderItem existing = findItem(productId);
        int remainingQuantity = existing.quantity() - quantity;

        if (remainingQuantity < 0) {
            throw new DomainException("Quantity to remove cannot be greater than existing item quantity.");
        }

        if (remainingQuantity == 0 && orderItems.size() == 1) {
            throw new DomainException("Order must contain at least one item.");
        }

        if (remainingQuantity == 0) {
            orderItems.remove(productId);
        } else {
            orderItems.put(productId, existing.decreaseQuantityBy(quantity));
        }
    }

    public void applyDiscount(ProductId productId, Discount discount) {
        validateNotCompleted();
        Objects.requireNonNull(productId, "Product ID cannot be null");
        Objects.requireNonNull(discount, "Discount cannot be null");
        OrderItem existing = findItem(productId);
        orderItems.put(productId, existing.applyDiscount(discount));
    }

    public Price grossTotalPrice() {
        return orderItems.values().stream()
                .map(OrderItem::grossTotalPrice)
                .reduce(Price.zero(currency()), Price::add);
    }

    public Price totalPrice() {
        return orderItems.values().stream()
                .map(OrderItem::totalPrice)
                .reduce(Price.zero(currency()), Price::add);
    }

    private OrderItem findItem(ProductId productId) {
        var item = orderItems.get(productId);
        if (item == null) throw new DomainException("Product with ID " + productId + " does not exist in order");
        return item;
    }

    private Currency currency() {
        return orderItems.values().iterator().next().unitPrice().currency();
    }

    private void validateSameCurrency(OrderItem item) {
        if (orderItems.isEmpty()) {
            return;
        }

        if (!currency().equals(item.unitPrice().currency())) {
            throw new DomainException("All order items must have the same currency");
        }
    }

    private void validateCurrencyConsistency() {
        Currency currency = currency();

        boolean hasDifferentCurrency = orderItems.values().stream()
                .anyMatch(item -> !currency.equals(item.unitPrice().currency()));

        if (hasDifferentCurrency) {
            throw new DomainException("All order items must have the same currency");
        }
    }

    private void validateNotCompleted() {
        if (OrderStatus.COMPLETED.equals(status)) throw new DomainException("Completed order cannot be changed");
    }

    public OrderId id() {
        return id;
    }

    public OrderStatus status() {
        return status;
    }

    public List<OrderItem> items() {
        return List.copyOf(orderItems.values());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", items=" + orderItems.values() +
                ", totalPrice=" + totalPrice() +
                '}';
    }
}