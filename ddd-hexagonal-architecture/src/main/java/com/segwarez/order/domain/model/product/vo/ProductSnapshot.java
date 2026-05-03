package com.segwarez.order.domain.model.product.vo;

import com.segwarez.order.domain.model.pricing.vo.Price;

import java.util.Objects;

public final class ProductSnapshot {
    private final ProductId productId;
    private final ProductName productName;
    private final Price unitPrice;

    private ProductSnapshot(ProductId productId, ProductName productName, Price unitPrice) {
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        this.productName = Objects.requireNonNull(productName, "Product name cannot be null");
        this.unitPrice = Objects.requireNonNull(unitPrice, "Unit price cannot be null");
    }

    public static ProductSnapshot of(ProductId productId, ProductName productName, Price unitPrice) {
        return new ProductSnapshot(productId, productName, unitPrice);
    }

    public ProductId productId() {
        return productId;
    }

    public ProductName productName() {
        return productName;
    }

    public Price unitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductSnapshot other)) return false;
        return productId.equals(other.productId) && productName.equals(other.productName) && unitPrice.equals(other.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, unitPrice);
    }

    @Override
    public String toString() {
        return "ProductSnapshot{" +
                "productId=" + productId +
                ", productName=" + productName +
                ", unitPrice=" + unitPrice +
                '}';
    }
}