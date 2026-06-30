package com.vet.appointment_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {
    private Long id;
    private Long clienteId;
    private Long mascotaId;
    private java.time.LocalDateTime appointmentDate;
    private String reason;
    private String status;
}
