package com.vet.animalink.cliente_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de SALIDA: lo que el sistema devuelve al consumidor.
 * Solo expone información NO sensible. El RUT viaja ENMASCARADO
 * y NO se exponen campos internos como el estado "activo".
 */
@Schema(description = "Información pública de un cliente (datos sensibles ocultos)")
public record ClienteResponse(

        @Schema(description = "Identificador del cliente", example = "1")
        Long id,

        @Schema(description = "Nombre", example = "María")
        String nombre,

        @Schema(description = "Apellido", example = "González")
        String apellido,

        @Schema(description = "Correo electrónico", example = "maria.gonzalez@correo.cl")
        String email,

        @Schema(description = "Teléfono", example = "+56912345678")
        String telefono,

        @Schema(description = "Dirección", example = "Av. Siempre Viva 742, Santiago")
        String direccion,

        @Schema(description = "RUT enmascarado (dato sensible protegido)", example = "********-9")
        String rut
) {}
