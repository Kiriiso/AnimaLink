package com.vet.animalink.historial_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Entrada del historial clínico (con nombre de la mascota)")
public record HistorialResponse(
        Long id,
        Long mascotaId,
        @Schema(description = "Nombre de la mascota (de mascota_service)") String mascotaNombre,
        LocalDate fecha,
        String diagnostico,
        String tratamiento,
        String observaciones
) {}
