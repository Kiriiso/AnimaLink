package com.vet.animalink.factura_service.client.dto;

import java.math.BigDecimal;

/** Vista parcial del insumo devuelta por inventario_service. */
public record InsumoDTO(
        Long id,
        String nombre,
        BigDecimal precioVenta,
        Integer stock
) {}
