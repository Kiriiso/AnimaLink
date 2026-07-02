package com.vet.animalink.control_alta_service.model;

import com.vet.animalink.control_alta_service.model.enums.EstadoControl;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Control de ingreso/alta (hospitalización) de una mascota.
 */
@Entity
@Table(name = "controles_alta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Registro de hospitalización (ingreso y alta) de una mascota")
public class ControlAlta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "ID de la mascota (vive en mascota_service)", example = "5")
    @Column(name = "mascota_id", nullable = false)
    private Long mascotaId;

    @Schema(description = "Fecha y hora de ingreso", example = "2026-06-20T14:00:00")
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;

    @Schema(description = "Fecha y hora de alta (null si sigue hospitalizado)")
    @Column(name = "fecha_alta")
    private LocalDateTime fechaAlta;

    @Schema(description = "Motivo del ingreso", example = "Post-operatorio de cirugía")
    @Column(name = "motivo_ingreso", nullable = false)
    private String motivoIngreso;

    @Schema(description = "Estado del control", example = "HOSPITALIZADO")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoControl estado = EstadoControl.HOSPITALIZADO;

    @Schema(description = "Observaciones")
    @Column(length = 500)
    private String observaciones;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
