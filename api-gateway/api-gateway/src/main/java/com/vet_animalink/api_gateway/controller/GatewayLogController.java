package com.vet_animalink.api_gateway.controller;

import com.vet_animalink.api_gateway.dto.GatewayLogRequestDTO;
import com.vet_animalink.api_gateway.dto.GatewayLogResponseDTO;
import com.vet_animalink.api_gateway.service.impl.GatewayLogServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/logs")
public class GatewayLogController {

    private final GatewayLogServiceImpl service;

    public GatewayLogController(GatewayLogServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<GatewayLogResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAllDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GatewayLogResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdDTO(id));
    }

    @PostMapping
    public ResponseEntity<GatewayLogResponseDTO> create(@Valid @RequestBody GatewayLogRequestDTO request) {
        GatewayLogResponseDTO saved = service.saveDTO(request);
        return ResponseEntity.created(URI.create("/logs/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GatewayLogResponseDTO> update(@PathVariable Long id, @Valid @RequestBody GatewayLogRequestDTO request) {
        return ResponseEntity.ok(service.updateDTO(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
