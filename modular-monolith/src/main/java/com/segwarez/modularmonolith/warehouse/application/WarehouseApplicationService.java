package com.segwarez.modularmonolith.warehouse.application;

import com.segwarez.modularmonolith.warehouse.api.WarehouseFacade;
import com.segwarez.modularmonolith.warehouse.api.WarehouseProductItem;
import com.segwarez.modularmonolith.warehouse.domain.StockItem;
import com.segwarez.modularmonolith.warehouse.infrastructure.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseApplicationService implements WarehouseFacade {
    private final WarehouseRepository warehouseRepository;

    @Override
    //@Transactional
    public boolean reserveProducts(List<WarehouseProductItem> products) {
        Map<UUID, StockItem> stockByProductId = fetchStockItems(products);

        if (!isEveryItemAvailable(products, stockByProductId)) {
            return false;
        }

        reduceStock(products, stockByProductId);
        warehouseRepository.updateAll(stockByProductId.values());
        return true;
    }

    private Map<UUID, StockItem> fetchStockItems(List<WarehouseProductItem> products) {
        List<UUID> productIds = products.stream()
                .map(WarehouseProductItem::getProductId)
                .toList();

        return warehouseRepository.findByProductIds(productIds).stream()
                .collect(Collectors.toMap(StockItem::getProductId, Function.identity()));
    }

    private boolean isEveryItemAvailable(List<WarehouseProductItem> products, Map<UUID, StockItem> stockByProductId) {
        return products.stream()
                .allMatch(product -> Optional.ofNullable(stockByProductId.get(product.getProductId()))
                        .filter(stock -> stock.check(product.getQuantity()))
                        .isPresent());
    }

    private void reduceStock(List<WarehouseProductItem> products, Map<UUID, StockItem> stockByProductId) {
        products.forEach(product ->
                Optional.ofNullable(stockByProductId.get(product.getProductId()))
                        .ifPresent(stock -> stock.decrease(product.getQuantity()))
        );
    }
}