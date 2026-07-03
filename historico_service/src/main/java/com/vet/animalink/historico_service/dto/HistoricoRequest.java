package com.vet.animalink.historico_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Datos requeridos para registrar un nuevo histórico de venta")
public record HistoricoRequest(

        @Schema(example = "10000.00", description = "Total neto de la venta")
        @NotNull(message = "El total neto es obligatorio")
        @DecimalMin(value = "0.00", message = "El total neto no puede ser menor a 0")
        BigDecimal totalNeto,

        @Schema(example = "500.00", description = "Monto de descuento aplicado")
        @NotNull(message = "El descuento es obligatorio")
        @DecimalMin(value = "0.00", message = "El descuento no puede ser menor a 0")
        BigDecimal descuento,

        @Schema(example = "1900.00", description = "Monto de IVA calculado")
        @NotNull(message = "El IVA es obligatorio")
        @DecimalMin(value = "0.00", message = "El IVA no puede ser menor a 0")
        BigDecimal iva,

        @Schema(example = "11400.00", description = "Total final de la transacción")
        @NotNull(message = "El total de la venta es obligatorio")
        @DecimalMin(value = "0.00", message = "El total de la venta no puede ser menor a 0")
        BigDecimal totalVenta,

        @Schema(example = "EFECTIVO", description = "Método de pago utilizado")
        @NotBlank(message = "El método de pago es obligatorio")
        String metodoPago,

        @Schema(description = "Fecha y hora de la venta. Si se envía null, el sistema asignará la fecha actual automáticamente")
        LocalDateTime fechaVenta
) {
}