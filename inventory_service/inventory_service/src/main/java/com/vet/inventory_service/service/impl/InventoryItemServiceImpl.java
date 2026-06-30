package com.vet.inventory_service.service.impl;

import com.vet.inventory_service.dto.InventoryItemRequestDTO;
import com.vet.inventory_service.dto.InventoryItemResponseDTO;
import com.vet.inventory_service.exception.ResourceNotFoundException;
import com.vet.inventory_service.mapper.InventoryItemMapper;
import com.vet.inventory_service.model.InventoryItem;
import com.vet.inventory_service.repository.InventoryItemRepository;
import com.vet.inventory_service.service.InventoryItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository repository;
    private final InventoryItemMapper mapper;

    public InventoryItemServiceImpl(InventoryItemRepository repository, InventoryItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public InventoryItem save(InventoryItem entity) {
        return repository.save(entity);
    }

    @Override
    public InventoryItem update(Long id, InventoryItem entity) {
        InventoryItem existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("InventoryItem not found with id " + id));        existing.setName(entity.getName());
        existing.setSku(entity.getSku());
        existing.setQuantity(entity.getQuantity());
        existing.setPrice(entity.getPrice());
        return repository.save(existing);
    }

    @Override
    public InventoryItem findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("InventoryItem not found with id " + id));
    }

    @Override
    public java.util.List<InventoryItem> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("InventoryItem not found with id " + id);
        }
        repository.deleteById(id);
    }

    public InventoryItemResponseDTO saveDTO(InventoryItemRequestDTO dto) {
        InventoryItem entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public InventoryItemResponseDTO updateDTO(Long id, InventoryItemRequestDTO dto) {
        InventoryItem entity = mapper.toEntity(dto);
        return mapper.toDTO(update(id, entity));
    }

    public InventoryItemResponseDTO findByIdDTO(Long id) {
        return mapper.toDTO(findById(id));
    }

    public java.util.List<InventoryItemResponseDTO> findAllDTO() {
        return mapper.toDTOList(findAll());
    }
}
