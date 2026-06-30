package com.vet.factura_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaResponseDTO {
    private Long id;
    private Long clienteId;
    private java.math.BigDecimal amount;
    private String description;
    private boolean paid;
    private java.time.LocalDate issueDate;
}
