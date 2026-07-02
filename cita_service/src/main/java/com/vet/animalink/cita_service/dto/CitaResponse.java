package com.vet.animalink.cita_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO de salida. Incluye el nombre de la mascota y del veterinario
 * (traídos desde mascota_service y usuario_service).
 */
@Schema(description = "Información de una cita")
public record CitaResponse(
        Long id,
        Long mascotaId,
        @Schema(description = "Nombre de la mascota (de mascota_service)") String mascotaNombre,
        Long veterinarioId,
        @Schema(description = "Nombre del veterinario (de usuario_service)") String veterinarioNombre,
        LocalDateTime fechaHora,
        String motivo,
        String estado,
        String observaciones
) {}
