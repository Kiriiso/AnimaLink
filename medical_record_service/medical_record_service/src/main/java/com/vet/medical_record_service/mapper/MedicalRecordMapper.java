package com.vet.medical_record_service.mapper;

import com.vet.medical_record_service.dto.MedicalRecordRequestDTO;
import com.vet.medical_record_service.dto.MedicalRecordResponseDTO;
import com.vet.medical_record_service.model.MedicalRecord;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MedicalRecordMapper {

    public MedicalRecord toEntity(MedicalRecordRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        MedicalRecord entity = new MedicalRecord();
        entity.setId(dto.getId());        entity.setMascotaId(dto.getMascotaId());
        entity.setVeterinarian(dto.getVeterinarian());
        entity.setVisitDate(dto.getVisitDate());
        entity.setNotes(dto.getNotes());
        return entity;
    }

    public MedicalRecordResponseDTO toDTO(MedicalRecord entity) {
        if (entity == null) {
            return null;
        }
        MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO();
        dto.setId(entity.getId());        dto.setMascotaId(entity.getMascotaId());
        dto.setVeterinarian(entity.getVeterinarian());
        dto.setVisitDate(entity.getVisitDate());
        dto.setNotes(entity.getNotes());
        return dto;
    }

    public java.util.List<MedicalRecordResponseDTO> toDTOList(java.util.List<MedicalRecord> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
