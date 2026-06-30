package com.vet.medical_record_service.service;

import com.vet.medical_record_service.model.MedicalRecord;
import java.util.List;

public interface MedicalRecordService {

    MedicalRecord save(MedicalRecord entity);

    MedicalRecord update(Long id, MedicalRecord entity);

    MedicalRecord findById(Long id);

    List<MedicalRecord> findAll();

    void deleteById(Long id);
}
