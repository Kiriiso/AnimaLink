package com.vet.animalink.control_alta_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Información de un control de hospitalización")
public record ControlAltaResponse(
        Long id,
        Long mascotaId,
        @Schema(description = "Nombre de la mascota (de mascota_service)") String mascotaNombre,
        LocalDateTime fechaIngreso,
        LocalDateTime fechaAlta,
        String motivoIngreso,
        String estado,
        String observaciones
) {}
