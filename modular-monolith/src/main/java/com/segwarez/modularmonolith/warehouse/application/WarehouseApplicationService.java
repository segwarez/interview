package com.segwarez.modularmonolith.warehouse.application;

import com.segwarez.modularmonolith.warehouse.api.WarehouseFacade;
import com.segwarez.modularmonolith.warehouse.api.WarehouseProductItem;
import com.segwarez.modularmonolith.warehouse.domain.StockItem;
import com.segwarez.modularmonolith.warehouse.infrastructure.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseApplicationService implements WarehouseFacade {
    private final WarehouseRepository warehouseRepository;

    @Override
    public boolean reserve(List<WarehouseProductItem> products) {
        Map<UUID, StockItem> stockByProductId = products.stream()
                .collect(Collectors.toMap(
                        WarehouseProductItem::productId,
                        p -> warehouseRepository.getByProductId(p.productId())));

        boolean allAvailable = products.stream()
                .allMatch(p -> stockByProductId
                        .get(p.productId())
                        .check(p.quantity()));

        if (!allAvailable) {
            return false;
        }

        products.forEach(p ->
                stockByProductId.get(p.productId())
                        .decrease(p.quantity())
        );

        return true;
    }
}