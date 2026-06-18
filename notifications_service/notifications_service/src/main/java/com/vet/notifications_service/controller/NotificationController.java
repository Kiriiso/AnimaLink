package com.vet.notifications_service.controller;

import com.vet.notifications_service.dto.NotificationRequestDTO;
import com.vet.notifications_service.dto.NotificationResponseDTO;
import com.vet.notifications_service.service.impl.NotificationServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationServiceImpl service;

    public NotificationController(NotificationServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAllDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdDTO(id));
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> create(@Valid @RequestBody NotificationRequestDTO request) {
        NotificationResponseDTO saved = service.saveDTO(request);
        return ResponseEntity.created(URI.create("/notifications/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> update(@PathVariable Long id, @Valid @RequestBody NotificationRequestDTO request) {
        return ResponseEntity.ok(service.updateDTO(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
