package com.vet.animalink.historial_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Registro del historial clínico de una mascota.
 */
@Entity
@Table(name = "historiales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entrada del historial clínico de una mascota")
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "ID de la mascota (vive en mascota_service)", example = "5")
    @Column(nullable = false)
    private Long mascotaId;

    @Schema(description = "Fecha de la atención", example = "2026-06-20")
    @Column(nullable = false)
    private LocalDate fecha;

    @Schema(description = "Diagnóstico", example = "Otitis externa")
    @Column(nullable = false)
    private String diagnostico;

    @Schema(description = "Tratamiento indicado", example = "Limpieza y antibiótico tópico")
    @Column(length = 500)
    private String tratamiento;

    @Schema(description = "Observaciones del veterinario")
    @Column(length = 500)
    private String observaciones;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
