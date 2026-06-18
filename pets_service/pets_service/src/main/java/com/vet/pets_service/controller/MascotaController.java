package com.vet.pets_service.controller;

import com.vet.pets_service.dto.MascotaRequestDTO;
import com.vet.pets_service.dto.MascotaResponseDTO;
import com.vet.pets_service.service.impl.MascotaServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/mascotas")
public class MascotaController {

    private final MascotaServiceImpl service;

    public MascotaController(MascotaServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MascotaResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAllDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdDTO(id));
    }

    @PostMapping
    public ResponseEntity<MascotaResponseDTO> create(@Valid @RequestBody MascotaRequestDTO request) {
        MascotaResponseDTO saved = service.saveDTO(request);
        return ResponseEntity.created(URI.create("/mascotas/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody MascotaRequestDTO request) {
        return ResponseEntity.ok(service.updateDTO(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
