package com.segwarez.modularmonolith.warehouse.infrastructure.repository;

import com.segwarez.modularmonolith.warehouse.domain.StockItem;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class WarehouseRepository {
    private final Map<UUID, StockItem> stockItems = new ConcurrentHashMap<>();

    @PostConstruct
    void initData() {
        UUID p1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID p2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

        stockItems.put(p1, new StockItem(p1, 100));
        stockItems.put(p2, new StockItem(p2, 50));
    }

    public List<StockItem> findByProductIds(List<UUID> productIds) {
        return productIds.stream()
                .distinct()
                .map(stockItems::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public void updateAll(Collection<StockItem> updatedStockItems) {
        updatedStockItems.forEach(item -> stockItems.put(item.getProductId(), item));
    }
}
