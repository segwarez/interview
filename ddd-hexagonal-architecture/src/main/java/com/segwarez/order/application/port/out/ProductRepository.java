package com.segwarez.order.application.port.out;

import com.segwarez.order.domain.model.product.Product;
import com.segwarez.order.domain.model.product.vo.ProductId;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(ProductId productId);
}