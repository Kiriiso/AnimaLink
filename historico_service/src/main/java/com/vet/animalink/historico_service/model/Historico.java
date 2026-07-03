package com.vet.animalink.historico_service.model; 

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Historial de ventas registradas en el sistema")
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del registro histórico", example = "1")
    private Long id;

    @Schema(description = "Total neto del histórico", example = "10000.00")
    @NotNull(message = "El total neto es obligatorio")
    @DecimalMin(value = "0.00", message = "El total neto no puede ser menor a 0")
    @Column(name = "total_neto", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalNeto = BigDecimal.ZERO;

    @Schema(description = "Descuento aplicado al histórico", example = "500.00")
    @NotNull(message = "El descuento es obligatorio")
    @DecimalMin(value = "0.00", message = "El descuento no puede ser menor a 0")
    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal descuento = BigDecimal.ZERO;

    @Schema(description = "IVA de la transacción", example = "1900.00")
    @NotNull(message = "El IVA es obligatorio")
    @DecimalMin(value = "0.00", message = "El IVA no puede ser menor a 0")
    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal iva = BigDecimal.ZERO;
    
    @Schema(description = "Total final de la venta", example = "11400.00")
    @NotNull(message = "El total de la venta es obligatorio")
    @DecimalMin(value = "0.00", message = "El total de la venta no puede ser menor a 0")
    @Column(name = "total_venta", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalVenta = BigDecimal.ZERO;

    @Schema(description = "Método de pago utilizado", example = "EFECTIVO")
    @NotBlank(message = "El método de pago es obligatorio")
    @Column(name = "metodo_pago", nullable = false, length = 50)
    private String metodoPago;

    @Schema(description = "Fecha y hora en que se efectuó la venta")
    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;

    @Schema(description = "Fecha de creación del registro en la base de datos", hidden = true)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.fechaVenta == null) {
            this.fechaVenta = LocalDateTime.now();
        }
        this.createdAt = LocalDateTime.now();
    }
}