package com.vet.animalink.inventario_service.model;

import com.vet.animalink.inventario_service.model.enums.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Insumo / producto del inventario (medicamentos, vacunas, alimentos, etc.).
 */
@Entity
@Table(name = "insumos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Insumo o producto del inventario veterinario")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del insumo", example = "Amoxicilina 500mg")
    @Column(nullable = false)
    private String nombre;

    @Schema(description = "Categoría", example = "MEDICAMENTO")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Schema(description = "Descripción")
    private String descripcion;

    @Schema(description = "Stock disponible", example = "120")
    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    @Schema(description = "Stock mínimo de alerta", example = "10")
    @Column(nullable = false)
    @Builder.Default
    private Integer stockMinimo = 5;

    @Schema(description = "Precio de COSTO (dato sensible, NO se expone)", example = "1500.00")
    @Column(precision = 10, scale = 2)
    private BigDecimal precioCosto;

    @Schema(description = "Precio de venta al público", example = "2500.00")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Schema(description = "Fecha de vencimiento", example = "2027-01-31")
    private LocalDate fechaVencimiento;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
