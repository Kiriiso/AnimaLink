package com.vet.animalink.inventario_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de salida. NO expone el precio de COSTO (dato sensible del negocio),
 * solo el precio de venta al público.
 */
@Schema(description = "Información pública de un insumo (precio de costo oculto)")
public record InsumoResponse(
        Long id,
        String nombre,
        String categoria,
        String descripcion,
        Integer stock,
        Integer stockMinimo,
        @Schema(description = "Precio de venta al público") BigDecimal precioVenta,
        LocalDate fechaVencimiento,
        @Schema(description = "Indica si el stock está bajo el mínimo") boolean bajoStock
) {}
