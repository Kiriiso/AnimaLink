package com.vet.medical_record_service.controller;

import com.vet.medical_record_service.dto.MedicalRecordRequestDTO;
import com.vet.medical_record_service.dto.MedicalRecordResponseDTO;
import com.vet.medical_record_service.service.impl.MedicalRecordServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordController {

    private final MedicalRecordServiceImpl service;

    public MedicalRecordController(MedicalRecordServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAllDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdDTO(id));
    }

    @PostMapping
    public ResponseEntity<MedicalRecordResponseDTO> create(@Valid @RequestBody MedicalRecordRequestDTO request) {
        MedicalRecordResponseDTO saved = service.saveDTO(request);
        return ResponseEntity.created(URI.create("/medical-records/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> update(@PathVariable Long id, @Valid @RequestBody MedicalRecordRequestDTO request) {
        return ResponseEntity.ok(service.updateDTO(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
