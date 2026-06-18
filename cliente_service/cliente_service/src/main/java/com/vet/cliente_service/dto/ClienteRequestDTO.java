package com.vet.cliente_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
}
