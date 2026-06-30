package com.vet.animalink.factura_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Factura con su detalle y el nombre del cliente")
public record FacturaResponse(
        Long id,
        Long clienteId,
        @Schema(description = "Nombre del cliente (de cliente_service)") String clienteNombre,
        LocalDateTime fecha,
        String estado,
        BigDecimal total,
        List<ItemResponse> items
) {
    @Schema(description = "Ítem de la factura")
    public record ItemResponse(
            Long insumoId,
            String descripcion,
            Integer cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal
    ) {}
}
