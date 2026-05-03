package com.segwarez.order.infrastructure.adapter.out.persistence.mongodb.mapper;

import com.segwarez.order.domain.model.discount.Discount;
import com.segwarez.order.domain.model.discount.vo.DiscountCode;
import com.segwarez.order.domain.model.discount.vo.DiscountPercentage;
import com.segwarez.order.domain.model.order.Order;
import com.segwarez.order.domain.model.order.OrderItem;
import com.segwarez.order.domain.model.order.OrderStatus;
import com.segwarez.order.domain.model.order.vo.*;
import com.segwarez.order.domain.model.pricing.vo.Price;
import com.segwarez.order.domain.model.product.vo.ProductId;
import com.segwarez.order.domain.model.product.vo.ProductName;
import com.segwarez.order.domain.model.product.vo.ProductSnapshot;
import com.segwarez.order.infrastructure.adapter.out.persistence.mongodb.document.OrderDocument;
import com.segwarez.order.infrastructure.adapter.out.persistence.mongodb.document.OrderItemDocument;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Objects;

@Component
public class OrderDocumentMapper {
    public OrderDocument toDocument(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");

        var document = new OrderDocument();
        document.setId(order.id().asString());
        document.setStatus(order.status().name());
        document.setItems(
                order.items().stream()
                        .map(this::toDocument)
                        .toList()
        );

        document.setTotalAmount(order.totalPrice().amount());
        document.setCurrency(order.totalPrice().currency().getCurrencyCode());

        return document;
    }

    private OrderItemDocument toDocument(OrderItem item) {
        var document = new OrderItemDocument();

        document.setProductId(item.productId().asString());
        document.setProductName(item.productName().value());
        document.setUnitPriceAmount(item.unitPrice().amount());
        document.setCurrency(item.unitPrice().currency().getCurrencyCode());
        document.setQuantity(item.quantity());
        if (!item.discount().isNone()) {
            document.setDiscountCode(item.discount().code().value());
            document.setDiscountPercentage(item.discount().percentage().value());
        }

        return document;
    }

    public Order toDomain(OrderDocument document) {
        Objects.requireNonNull(document, "OrderDocument cannot be null");

        var items = document.getItems().stream()
                .map(this::toDomain)
                .toList();

        return Order.restore(
                OrderId.of(document.getId()),
                OrderStatus.valueOf(document.getStatus()),
                items
        );
    }

    private OrderItem toDomain(OrderItemDocument document) {
        var product = ProductSnapshot.of(
                ProductId.of(document.getProductId()),
                ProductName.of(document.getProductName()),
                Price.of(
                        document.getUnitPriceAmount(),
                        Currency.getInstance(document.getCurrency())
                )
        );

        Discount discount;
        if (document.getDiscountCode() == null) {
            discount = Discount.none();
        } else {
            discount = Discount.of(
                    DiscountCode.of(document.getDiscountCode()),
                    DiscountPercentage.of(document.getDiscountPercentage())
            );
        }

        return OrderItem.of(product, discount, document.getQuantity());
    }
}