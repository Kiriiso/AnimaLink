package com.vet_animalink.api_gateway.service.impl;

import com.vet_animalink.api_gateway.dto.GatewayLogRequestDTO;
import com.vet_animalink.api_gateway.dto.GatewayLogResponseDTO;
import com.vet_animalink.api_gateway.exception.ResourceNotFoundException;
import com.vet_animalink.api_gateway.mapper.GatewayLogMapper;
import com.vet_animalink.api_gateway.model.GatewayLog;
import com.vet_animalink.api_gateway.repository.GatewayLogRepository;
import com.vet_animalink.api_gateway.service.GatewayLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatewayLogServiceImpl implements GatewayLogService {

    private final GatewayLogRepository repository;
    private final GatewayLogMapper mapper;

    public GatewayLogServiceImpl(GatewayLogRepository repository, GatewayLogMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public GatewayLog save(GatewayLog entity) {
        return repository.save(entity);
    }

    @Override
    public GatewayLog update(Long id, GatewayLog entity) {
        GatewayLog existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("GatewayLog not found with id " + id));        existing.setPath(entity.getPath());
        existing.setMethod(entity.getMethod());
        existing.setStatus(entity.getStatus());
        existing.setTimestamp(entity.getTimestamp());
        return repository.save(existing);
    }

    @Override
    public GatewayLog findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("GatewayLog not found with id " + id));
    }

    @Override
    public java.util.List<GatewayLog> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("GatewayLog not found with id " + id);
        }
        repository.deleteById(id);
    }

    public GatewayLogResponseDTO saveDTO(GatewayLogRequestDTO dto) {
        GatewayLog entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public GatewayLogResponseDTO updateDTO(Long id, GatewayLogRequestDTO dto) {
        GatewayLog entity = mapper.toEntity(dto);
        return mapper.toDTO(update(id, entity));
    }

    public GatewayLogResponseDTO findByIdDTO(Long id) {
        return mapper.toDTO(findById(id));
    }

    public java.util.List<GatewayLogResponseDTO> findAllDTO() {
        return mapper.toDTOList(findAll());
    }
}
