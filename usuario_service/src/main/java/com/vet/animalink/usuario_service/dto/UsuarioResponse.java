package com.vet.animalink.usuario_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de salida. Expone solo datos públicos; el RUT viaja enmascarado
 * y no se exponen campos internos (activo, createdAt).
 */
@Schema(description = "Información pública de un usuario/funcionario")
public record UsuarioResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        String telefono,
        String rol,
        String especialidad,

        @Schema(description = "RUT enmascarado", example = "********-3")
        String rut
) {}
