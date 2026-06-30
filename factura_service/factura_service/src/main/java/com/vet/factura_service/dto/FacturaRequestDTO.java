package com.vet.factura_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaRequestDTO {
    private Long id;
    private Long clienteId;
    private java.math.BigDecimal amount;
    private String description;
    private boolean paid;
    private java.time.LocalDate issueDate;
}
