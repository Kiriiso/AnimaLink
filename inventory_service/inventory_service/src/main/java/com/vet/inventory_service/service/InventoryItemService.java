package com.vet.inventory_service.service;

import com.vet.inventory_service.model.InventoryItem;
import java.util.List;

public interface InventoryItemService {

    InventoryItem save(InventoryItem entity);

    InventoryItem update(Long id, InventoryItem entity);

    InventoryItem findById(Long id);

    List<InventoryItem> findAll();

    void deleteById(Long id);
}
