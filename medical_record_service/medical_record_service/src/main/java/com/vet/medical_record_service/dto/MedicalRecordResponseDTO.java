package com.vet.medical_record_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponseDTO {
    private Long id;
    private Long mascotaId;
    private String veterinarian;
    private java.time.LocalDate visitDate;
    private String notes;
}
