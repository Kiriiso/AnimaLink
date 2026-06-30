package com.vet.auth_service.controller;

import com.vet.auth_service.dto.AuthUserRequestDTO;
import com.vet.auth_service.dto.AuthUserResponseDTO;
import com.vet.auth_service.service.impl.AuthUserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/authusers")
public class AuthUserController {

    private final AuthUserServiceImpl service;

    public AuthUserController(AuthUserServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AuthUserResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAllDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthUserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdDTO(id));
    }

    @PostMapping
    public ResponseEntity<AuthUserResponseDTO> create(@Valid @RequestBody AuthUserRequestDTO request) {
        AuthUserResponseDTO saved = service.saveDTO(request);
        return ResponseEntity.created(URI.create("/authusers/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthUserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AuthUserRequestDTO request) {
        return ResponseEntity.ok(service.updateDTO(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
