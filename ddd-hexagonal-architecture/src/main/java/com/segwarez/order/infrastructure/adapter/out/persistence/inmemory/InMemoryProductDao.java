package com.segwarez.order.infrastructure.adapter.out.persistence.inmemory;

import com.segwarez.order.application.port.out.ProductRepository;
import com.segwarez.order.domain.model.product.Product;
import com.segwarez.order.domain.model.pricing.vo.Price;
import com.segwarez.order.domain.model.product.vo.ProductId;
import com.segwarez.order.domain.model.product.vo.ProductName;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
class InMemoryProductDao implements ProductRepository {
    private final ConcurrentMap<ProductId, Product> products = new ConcurrentHashMap<>();

    InMemoryProductDao() {
        var product = Product.create(
                ProductId.of("11111111-1111-1111-1111-111111111111"),
                ProductName.of("iPhone 25"),
                Price.of(BigDecimal.valueOf(4999), Currency.getInstance("PLN"))
        );

        products.put(product.id(), product);
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return Optional.ofNullable(products.get(productId));
    }
}