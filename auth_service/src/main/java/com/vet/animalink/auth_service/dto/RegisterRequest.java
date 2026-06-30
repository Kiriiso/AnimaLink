package com.vet.animalink.auth_service.dto;

import com.vet.animalink.auth_service.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
    @NotBlank String username,
    @NotBlank String password,
    @NotNull Rol rol
) {}