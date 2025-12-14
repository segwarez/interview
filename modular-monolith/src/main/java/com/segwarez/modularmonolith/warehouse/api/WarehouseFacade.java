package com.segwarez.modularmonolith.warehouse.api;

import java.util.List;

public interface WarehouseFacade {
    boolean reserve(List<WarehouseProductItem> products);
}
