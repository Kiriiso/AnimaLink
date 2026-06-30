package com.vet.animalink.cliente_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Cliente = dueño de una o varias mascotas.
 * Esta entidad vive SOLO en la base de datos de este microservicio (animalink_cliente).
 */
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Cliente (dueño de mascotas) registrado en la veterinaria")
public class Cliente {

    @Schema(description = "Identificador único del cliente", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "RUT del cliente (dato sensible, se enmascara en las respuestas)", example = "18345678-9")
    @Column(nullable = false, unique = true, length = 15)
    private String rut;

    @Schema(description = "Nombre del cliente", example = "María")
    @Column(nullable = false)
    private String nombre;

    @Schema(description = "Apellido del cliente", example = "González")
    @Column(nullable = false)
    private String apellido;

    @Schema(description = "Correo electrónico", example = "maria.gonzalez@correo.cl")
    @Column(nullable = false, unique = true)
    private String email;

    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    @Column(length = 20)
    private String telefono;

    @Schema(description = "Dirección del cliente", example = "Av. Siempre Viva 742, Santiago")
    private String direccion;

    @Schema(description = "Indica si el cliente está activo en el sistema", example = "true")
    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @Schema(description = "Fecha de registro del cliente")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
