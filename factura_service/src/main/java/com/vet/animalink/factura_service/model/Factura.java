package com.vet.animalink.factura_service.model;

import com.vet.animalink.factura_service.model.enums.EstadoFactura;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Factura emitida a un cliente. Contiene su detalle (líneas) en la MISMA base de datos
 * de este microservicio (tablas facturas y detalles_factura). No comparte tablas con otros servicios.
 */
@Entity
@Table(name = "facturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Factura emitida a un cliente")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "ID del cliente (vive en cliente_service)", example = "3")
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Schema(description = "Fecha de emisión")
    @Column(nullable = false)
    private LocalDateTime fecha;

    @Schema(description = "Estado de la factura", example = "EMITIDA")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoFactura estado = EstadoFactura.EMITIDA;

    @Schema(description = "Total de la factura", example = "12500.00")
    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleFactura> detalles = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
        this.createdAt = LocalDateTime.now();
    }

    /** Agrega una línea y mantiene la relación bidireccional. */
    public void agregarDetalle(DetalleFactura detalle) {
        detalle.setFactura(this);
        this.detalles.add(detalle);
    }
}
