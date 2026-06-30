package com.vet.medical_record_service.service.impl;

import com.vet.medical_record_service.dto.MedicalRecordRequestDTO;
import com.vet.medical_record_service.dto.MedicalRecordResponseDTO;
import com.vet.medical_record_service.exception.ResourceNotFoundException;
import com.vet.medical_record_service.mapper.MedicalRecordMapper;
import com.vet.medical_record_service.model.MedicalRecord;
import com.vet.medical_record_service.repository.MedicalRecordRepository;
import com.vet.medical_record_service.service.MedicalRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository repository;
    private final MedicalRecordMapper mapper;

    public MedicalRecordServiceImpl(MedicalRecordRepository repository, MedicalRecordMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public MedicalRecord save(MedicalRecord entity) {
        return repository.save(entity);
    }

    @Override
    public MedicalRecord update(Long id, MedicalRecord entity) {
        MedicalRecord existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MedicalRecord not found with id " + id));        existing.setMascotaId(entity.getMascotaId());
        existing.setVeterinarian(entity.getVeterinarian());
        existing.setVisitDate(entity.getVisitDate());
        existing.setNotes(entity.getNotes());
        return repository.save(existing);
    }

    @Override
    public MedicalRecord findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MedicalRecord not found with id " + id));
    }

    @Override
    public java.util.List<MedicalRecord> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MedicalRecord not found with id " + id);
        }
        repository.deleteById(id);
    }

    public MedicalRecordResponseDTO saveDTO(MedicalRecordRequestDTO dto) {
        MedicalRecord entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public MedicalRecordResponseDTO updateDTO(Long id, MedicalRecordRequestDTO dto) {
        MedicalRecord entity = mapper.toEntity(dto);
        return mapper.toDTO(update(id, entity));
    }

    public MedicalRecordResponseDTO findByIdDTO(Long id) {
        return mapper.toDTO(findById(id));
    }

    public java.util.List<MedicalRecordResponseDTO> findAllDTO() {
        return mapper.toDTOList(findAll());
    }
}
