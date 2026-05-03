package com.segwarez.order.application.exception;

import com.segwarez.order.domain.model.product.vo.ProductId;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(ProductId productId) {
        super("Product not found: " + productId);
    }
}