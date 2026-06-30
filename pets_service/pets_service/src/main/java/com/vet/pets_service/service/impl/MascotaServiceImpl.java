package com.vet.pets_service.service.impl;

import com.vet.pets_service.dto.MascotaRequestDTO;
import com.vet.pets_service.dto.MascotaResponseDTO;
import com.vet.pets_service.exception.ResourceNotFoundException;
import com.vet.pets_service.mapper.MascotaMapper;
import com.vet.pets_service.model.Mascota;
import com.vet.pets_service.repository.MascotaRepository;
import com.vet.pets_service.service.MascotaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository repository;
    private final MascotaMapper mapper;

    public MascotaServiceImpl(MascotaRepository repository, MascotaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mascota save(Mascota entity) {
        return repository.save(entity);
    }

    @Override
    public Mascota update(Long id, Mascota entity) {
        Mascota existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mascota not found with id " + id));        existing.setNombre(entity.getNombre());
        existing.setEspecie(entity.getEspecie());
        existing.setRaza(entity.getRaza());
        existing.setClienteId(entity.getClienteId());
        return repository.save(existing);
    }

    @Override
    public Mascota findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mascota not found with id " + id));
    }

    @Override
    public java.util.List<Mascota> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Mascota not found with id " + id);
        }
        repository.deleteById(id);
    }

    public MascotaResponseDTO saveDTO(MascotaRequestDTO dto) {
        Mascota entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public MascotaResponseDTO updateDTO(Long id, MascotaRequestDTO dto) {
        Mascota entity = mapper.toEntity(dto);
        return mapper.toDTO(update(id, entity));
    }

    public MascotaResponseDTO findByIdDTO(Long id) {
        return mapper.toDTO(findById(id));
    }

    public java.util.List<MascotaResponseDTO> findAllDTO() {
        return mapper.toDTOList(findAll());
    }
}
