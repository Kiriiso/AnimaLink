package com.vet.animalink.mascota_service.model;

import com.vet.animalink.mascota_service.model.enums.Especie;
import com.vet.animalink.mascota_service.model.enums.Sexo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Mascota registrada en la veterinaria.
 * El dueño se referencia con clienteId (NO hay relación a la BD de otro microservicio).
 */
@Entity
@Table(name = "mascotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Mascota atendida en la veterinaria")
public class Mascota {

    @Schema(description = "Identificador único de la mascota", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre de la mascota", example = "Firulais")
    @Column(nullable = false)
    private String nombre;

    @Schema(description = "Especie", example = "PERRO")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especie especie;

    @Schema(description = "Raza", example = "Labrador")
    private String raza;

    @Schema(description = "Sexo", example = "MACHO")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sexo sexo;

    @Schema(description = "Fecha de nacimiento", example = "2020-05-12")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Schema(description = "Color del pelaje", example = "Café")
    private String color;

    @Schema(description = "ID del cliente dueño (vive en cliente_service)", example = "3")
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Schema(description = "Indica si la mascota está activa", example = "true")
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
