package com.segwarez.modularmonolith.warehouse.infrastructure.repository;

import com.segwarez.modularmonolith.warehouse.domain.StockItem;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class WarehouseRepository {

    private final Map<UUID, StockItem> storage = new ConcurrentHashMap<>();

    @PostConstruct
    void initDemoData() {
        UUID p1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID p2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

        storage.put(p1, new StockItem(p1, 100));
        storage.put(p2, new StockItem(p2, 50));
    }

    public StockItem getByProductId(UUID productId) {
        return storage.computeIfAbsent(productId, id -> new StockItem(id, 0));
    }
}
