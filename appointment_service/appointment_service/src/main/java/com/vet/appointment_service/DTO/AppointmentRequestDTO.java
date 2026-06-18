package com.vet.appointment_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {
    private Long id;
    private Long clienteId;
    private Long mascotaId;
    private java.time.LocalDateTime appointmentDate;
    private String reason;
    private String status;
}
