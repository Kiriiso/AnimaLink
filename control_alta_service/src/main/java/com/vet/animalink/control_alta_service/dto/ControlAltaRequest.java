package com.vet.animalink.control_alta_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Datos para registrar un ingreso (hospitalización)")
public record ControlAltaRequest(

        @Schema(example = "5")
        @NotNull(message = "La mascotaId es obligatoria")
        @Positive(message = "La mascotaId debe ser positiva")
        Long mascotaId,

        @Schema(example = "Post-operatorio de cirugía")
        @NotBlank(message = "El motivo de ingreso es obligatorio")
        String motivoIngreso,

        @Schema(example = "Mantener en observación 48 horas")
        String observaciones
) {}
