package com.vet.animalink.factura_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Línea/detalle de una factura. Referencia a un insumo (vive en inventario_service) por id.
 */
@Entity
@Table(name = "detalles_factura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Línea de detalle de una factura")
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id")
    @JsonIgnore
    private Factura factura;

    @Schema(description = "ID del insumo (vive en inventario_service)", example = "4")
    @Column(nullable = false)
    private Long insumoId;

    @Schema(description = "Descripción del insumo", example = "Amoxicilina 500mg")
    private String descripcion;

    @Schema(description = "Cantidad", example = "2")
    @Column(nullable = false)
    private Integer cantidad;

    @Schema(description = "Precio unitario (tomado de inventario_service)", example = "2500.00")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Schema(description = "Subtotal (cantidad * precioUnitario)", example = "5000.00")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;
}
