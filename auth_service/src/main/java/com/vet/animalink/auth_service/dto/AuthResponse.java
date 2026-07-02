package com.vet.animalink.auth_service.dto;

public record AuthResponse(
    String token,
    String username,
    String rol
) {}