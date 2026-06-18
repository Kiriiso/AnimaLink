package com.vet.factura_service.controller;

import com.vet.factura_service.dto.FacturaRequestDTO;
import com.vet.factura_service.dto.FacturaResponseDTO;
import com.vet.factura_service.service.impl.FacturaServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    private final FacturaServiceImpl service;

    public FacturaController(FacturaServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FacturaResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAllDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdDTO(id));
    }

    @PostMapping
    public ResponseEntity<FacturaResponseDTO> create(@Valid @RequestBody FacturaRequestDTO request) {
        FacturaResponseDTO saved = service.saveDTO(request);
        return ResponseEntity.created(URI.create("/facturas/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody FacturaRequestDTO request) {
        return ResponseEntity.ok(service.updateDTO(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
