package com.segwarez.order.domain.model.product;

import com.segwarez.order.domain.model.pricing.vo.Price;
import com.segwarez.order.domain.model.product.vo.ProductId;
import com.segwarez.order.domain.model.product.vo.ProductName;
import com.segwarez.order.domain.model.product.vo.ProductSnapshot;

import java.util.Objects;

public final class Product {
    private final ProductId id;
    private final ProductName name;
    private final Price price;

    private Product(ProductId id, ProductName name, Price price) {
        this.id = Objects.requireNonNull(id, "Product ID cannot be null");
        this.name = Objects.requireNonNull(name, "Product name cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");
    }

    public static Product create(ProductId id, ProductName name, Price price) {
        return new Product(id, name, price);
    }

    public static Product createNew(ProductName name, Price price) {
        return new Product(ProductId.generate(), name, price);
    }

    public ProductId id() {
        return id;
    }

    public ProductName name() {
        return name;
    }

    public Price price() {
        return price;
    }

    public ProductSnapshot snapshot() {
        return ProductSnapshot.of(id, name, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name=" + name +
                ", price=" + price +
                '}';
    }
}