package com.vet.animalink.historico_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Información detallada de un registro histórico de venta")
public record HistoricoResponse(
        @Schema(description = "ID único del registro histórico", example = "1")
        Long id,

        @Schema(description = "Total neto de la transacción", example = "10000.00")
        BigDecimal totalNeto,

        @Schema(description = "Descuento aplicado a la venta", example = "500.00")
        BigDecimal descuento,

        @Schema(description = "IVA cobrado en la transacción", example = "1900.00")
        BigDecimal iva,

        @Schema(description = "Total final cobrado de la venta", example = "11400.00")
        BigDecimal totalVenta,

        @Schema(description = "Método de pago utilizado", example = "EFECTIVO")
        String metodoPago,

        @Schema(description = "Fecha y hora en que se efectuó y registró la venta")
        LocalDateTime fechaVenta
) {
}