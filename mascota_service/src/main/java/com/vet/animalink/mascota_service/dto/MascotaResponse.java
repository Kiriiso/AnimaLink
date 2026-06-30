package com.vet.animalink.mascota_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * DTO de salida. No expone campos internos (activo, createdAt).
 * Incluye el nombre del dueño obtenido desde cliente_service (flujo entre microservicios).
 */
@Schema(description = "Información pública de una mascota")
public record MascotaResponse(

        Long id,
        String nombre,
        String especie,
        String raza,
        String sexo,
        LocalDate fechaNacimiento,
        String color,

        @Schema(description = "ID del dueño", example = "3")
        Long duenoId,

        @Schema(description = "Nombre del dueño (traído de cliente_service)", example = "María González")
        String duenoNombre
) {}
