package com.vet.animalink.cita_service.model;

import com.vet.animalink.cita_service.model.enums.EstadoCita;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Cita / agendamiento de atención veterinaria.
 * Referencia a la mascota (mascota_service) y al veterinario (usuario_service) por id.
 */
@Entity
@Table(name = "citas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Cita agendada en la veterinaria")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "ID de la mascota (vive en mascota_service)", example = "5")
    @Column(nullable = false)
    private Long mascotaId;

    @Schema(description = "ID del veterinario (vive en usuario_service)", example = "2")
    @Column(nullable = false)
    private Long veterinarioId;

    @Schema(description = "Fecha y hora de la cita", example = "2026-07-15T10:30:00")
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Schema(description = "Motivo de la consulta", example = "Vacunación anual")
    @Column(nullable = false)
    private String motivo;

    @Schema(description = "Estado de la cita", example = "PROGRAMADA")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoCita estado = EstadoCita.PROGRAMADA;

    @Schema(description = "Observaciones adicionales")
    @Column(length = 500)
    private String observaciones;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
