package com.vet.animalink.usuario_service.dto;

import com.vet.animalink.usuario_service.model.enums.Rol;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Datos para registrar o actualizar un usuario/funcionario")
public record UsuarioRequest(

        @NotBlank(message = "El RUT es obligatorio")
        @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "El RUT debe tener el formato 12345678-9")
        String rut,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        String apellido,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        String telefono,

        @NotNull(message = "El rol es obligatorio")
        Rol rol,

        @Schema(description = "Especialidad (solo para veterinarios)") //arreglar para que no sea mas estricto
        String especialidad
) {}
