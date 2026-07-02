package com.vet.animalink.usuario_service.model;

import com.vet.animalink.usuario_service.model.enums.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Personal del sistema (veterinarios, recepcionistas, administradores).
 * auth_service guarda las credenciales; aquí vive el PERFIL del trabajador.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Funcionario/personal de la veterinaria")
public class Usuario {

    @Schema(description = "Identificador del usuario", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "RUT (dato sensible, se enmascara)", example = "15987654-3")
    @Column(nullable = false, unique = true, length = 15)
    private String rut;

    @Schema(description = "Nombre", example = "Carlos")
    @Column(nullable = false)
    private String nombre;

    @Schema(description = "Apellido", example = "Pérez")
    @Column(nullable = false)
    private String apellido;

    @Schema(description = "Correo institucional", example = "carlos.perez@animalink.cl")
    @Column(nullable = false, unique = true)
    private String email;

    @Schema(description = "Teléfono", example = "+56998765432")
    @Column(length = 20)
    private String telefono;

    @Schema(description = "Rol del funcionario", example = "VETERINARIO")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Schema(description = "Especialidad (solo aplica a veterinarios)", example = "Cirugía")
    private String especialidad;

    @Schema(description = "Indica si el usuario está activo", example = "true")
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
