package com.vet.animalink.historial_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Schema(description = "Datos para registrar una entrada del historial clínico")
public record HistorialRequest(

        @Schema(example = "5")
        @NotNull(message = "La mascotaId es obligatoria")
        @Positive(message = "La mascotaId debe ser positiva")
        Long mascotaId,

        @Schema(example = "2026-06-20")
        @NotNull(message = "La fecha es obligatoria")
        @PastOrPresent(message = "La fecha no puede ser futura")
        LocalDate fecha,

        @Schema(example = "Otitis externa")
        @NotBlank(message = "El diagnóstico es obligatorio")
        String diagnostico,

        @Schema(example = "Limpieza y antibiótico tópico")
        String tratamiento,

        @Schema(example = "Controlar en 10 días")
        String observaciones
) {}
