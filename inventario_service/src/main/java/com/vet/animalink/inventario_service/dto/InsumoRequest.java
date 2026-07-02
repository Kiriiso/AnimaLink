package com.vet.animalink.inventario_service.dto;

import com.vet.animalink.inventario_service.model.enums.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Datos para crear o actualizar un insumo")
public record InsumoRequest(

        @Schema(example = "Amoxicilina 500mg")
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @Schema(example = "MEDICAMENTO")
        @NotNull(message = "La categoría es obligatoria")
        Categoria categoria,

        @Schema(example = "Antibiótico de amplio espectro")
        String descripcion,

        @Schema(example = "120")
        @NotNull(message = "El stock es obligatorio")
        @PositiveOrZero(message = "El stock no puede ser negativo")
        Integer stock,

        @Schema(example = "10")
        @PositiveOrZero(message = "El stock mínimo no puede ser negativo")
        Integer stockMinimo,

        @Schema(description = "Precio de costo (sensible)", example = "1500.00")
        @PositiveOrZero(message = "El precio de costo no puede ser negativo")
        BigDecimal precioCosto,

        @Schema(example = "2500.00")
        @NotNull(message = "El precio de venta es obligatorio")
        @PositiveOrZero(message = "El precio de venta no puede ser negativo")
        BigDecimal precioVenta,

        @Schema(example = "2027-01-31")
        LocalDate fechaVencimiento
) {}
