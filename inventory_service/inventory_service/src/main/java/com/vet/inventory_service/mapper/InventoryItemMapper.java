package com.vet.inventory_service.mapper;

import com.vet.inventory_service.dto.InventoryItemRequestDTO;
import com.vet.inventory_service.dto.InventoryItemResponseDTO;
import com.vet.inventory_service.model.InventoryItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryItemMapper {

    public InventoryItem toEntity(InventoryItemRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        InventoryItem entity = new InventoryItem();
        entity.setId(dto.getId());        entity.setName(dto.getName());
        entity.setSku(dto.getSku());
        entity.setQuantity(dto.getQuantity());
        entity.setPrice(dto.getPrice());
        return entity;
    }

    public InventoryItemResponseDTO toDTO(InventoryItem entity) {
        if (entity == null) {
            return null;
        }
        InventoryItemResponseDTO dto = new InventoryItemResponseDTO();
        dto.setId(entity.getId());        dto.setName(entity.getName());
        dto.setSku(entity.getSku());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getPrice());
        return dto;
    }

    public java.util.List<InventoryItemResponseDTO> toDTOList(java.util.List<InventoryItem> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
