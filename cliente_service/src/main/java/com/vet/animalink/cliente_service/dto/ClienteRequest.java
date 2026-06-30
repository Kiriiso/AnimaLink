package com.vet.animalink.cliente_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO de ENTRADA: datos que el cliente envía para crear/actualizar.
 * Incluye validaciones (reglas de negocio a nivel de aplicación).
 */
@Schema(description = "Datos necesarios para registrar o actualizar un cliente")
public record ClienteRequest(

        @Schema(description = "RUT del cliente", example = "18345678-9")
        @NotBlank(message = "El RUT es obligatorio")
        @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "El RUT debe tener el formato 12345678-9")
        String rut,

        @Schema(description = "Nombre", example = "María")
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @Schema(description = "Apellido", example = "González")
        @NotBlank(message = "El apellido es obligatorio")
        String apellido,

        @Schema(description = "Correo electrónico", example = "maria.gonzalez@correo.cl")
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        @Schema(description = "Teléfono", example = "+56912345678")
        String telefono,

        @Schema(description = "Dirección", example = "Av. Siempre Viva 742, Santiago")
        String direccion
) {}
