package com.vet.inventory_service.controller;

import com.vet.inventory_service.dto.InventoryItemRequestDTO;
import com.vet.inventory_service.dto.InventoryItemResponseDTO;
import com.vet.inventory_service.service.impl.InventoryItemServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryItemController {

    private final InventoryItemServiceImpl service;

    public InventoryItemController(InventoryItemServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAllDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdDTO(id));
    }

    @PostMapping
    public ResponseEntity<InventoryItemResponseDTO> create(@Valid @RequestBody InventoryItemRequestDTO request) {
        InventoryItemResponseDTO saved = service.saveDTO(request);
        return ResponseEntity.created(URI.create("/inventory/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> update(@PathVariable Long id, @Valid @RequestBody InventoryItemRequestDTO request) {
        return ResponseEntity.ok(service.updateDTO(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
