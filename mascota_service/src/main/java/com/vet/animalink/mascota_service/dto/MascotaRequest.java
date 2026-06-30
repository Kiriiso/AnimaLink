package com.vet.animalink.mascota_service.dto;

import com.vet.animalink.mascota_service.model.enums.Especie;
import com.vet.animalink.mascota_service.model.enums.Sexo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Schema(description = "Datos para registrar o actualizar una mascota")
public record MascotaRequest(

        @Schema(example = "Firulais")
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @Schema(example = "PERRO")
        @NotNull(message = "La especie es obligatoria")
        Especie especie,

        @Schema(example = "Labrador")
        String raza,

        @Schema(example = "MACHO")
        @NotNull(message = "El sexo es obligatorio")
        Sexo sexo,

        @Schema(example = "2020-05-12")
        @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
        LocalDate fechaNacimiento,

        @Schema(example = "Café")
        String color,

        @Schema(description = "ID del cliente dueño", example = "3")
        @NotNull(message = "El clienteId (dueño) es obligatorio")
        @Positive(message = "El clienteId debe ser positivo")
        Long clienteId
) {}
