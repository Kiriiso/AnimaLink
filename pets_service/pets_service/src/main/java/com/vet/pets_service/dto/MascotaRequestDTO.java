package com.vet.pets_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MascotaRequestDTO {
    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private Long clienteId;
}
