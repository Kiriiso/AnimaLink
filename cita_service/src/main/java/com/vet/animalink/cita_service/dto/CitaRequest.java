package com.vet.animalink.cita_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Schema(description = "Datos para agendar o actualizar una cita")
public record CitaRequest(

        @Schema(example = "5")
        @NotNull(message = "La mascotaId es obligatoria")
        @Positive(message = "La mascotaId debe ser positiva")
        Long mascotaId,

        @Schema(example = "2")
        @NotNull(message = "El veterinarioId es obligatorio")
        @Positive(message = "El veterinarioId debe ser positivo")
        Long veterinarioId,

        @Schema(example = "2026-07-15T10:30:00")
        @NotNull(message = "La fecha y hora son obligatorias")
        @Future(message = "La fecha de la cita debe ser futura")
        LocalDateTime fechaHora,

        @Schema(example = "Vacunación anual")
        @NotBlank(message = "El motivo es obligatorio")
        String motivo,

        @Schema(example = "Traer cartola de vacunas")
        String observaciones
) {}
