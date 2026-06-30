package com.vet.animalink.auth_service.controller;

import com.vet.animalink.auth_service.dto.AuthResponse;
import com.vet.animalink.auth_service.dto.LoginRequest;
import com.vet.animalink.auth_service.dto.RegisterRequest;
import com.vet.animalink.auth_service.service.UsuarioAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class usuarioAuthController {

    private final UsuarioAuthService usuarioAuthService;

    public usuarioAuthController(UsuarioAuthService usuarioAuthService) {
        this.usuarioAuthService = usuarioAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(usuarioAuthService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioAuthService.login(request));
    }
}