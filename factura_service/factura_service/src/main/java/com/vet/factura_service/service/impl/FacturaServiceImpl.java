package com.vet.factura_service.service.impl;

import com.vet.factura_service.dto.FacturaRequestDTO;
import com.vet.factura_service.dto.FacturaResponseDTO;
import com.vet.factura_service.exception.ResourceNotFoundException;
import com.vet.factura_service.mapper.FacturaMapper;
import com.vet.factura_service.model.Factura;
import com.vet.factura_service.repository.FacturaRepository;
import com.vet.factura_service.service.FacturaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository repository;
    private final FacturaMapper mapper;

    public FacturaServiceImpl(FacturaRepository repository, FacturaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Factura save(Factura entity) {
        return repository.save(entity);
    }

    @Override
    public Factura update(Long id, Factura entity) {
        Factura existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Factura not found with id " + id));        existing.setClienteId(entity.getClienteId());
        existing.setAmount(entity.getAmount());
        existing.setDescription(entity.getDescription());
        existing.setPaid(entity.isPaid());
        existing.setIssueDate(entity.getIssueDate());
        return repository.save(existing);
    }

    @Override
    public Factura findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Factura not found with id " + id));
    }

    @Override
    public java.util.List<Factura> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Factura not found with id " + id);
        }
        repository.deleteById(id);
    }

    public FacturaResponseDTO saveDTO(FacturaRequestDTO dto) {
        Factura entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public FacturaResponseDTO updateDTO(Long id, FacturaRequestDTO dto) {
        Factura entity = mapper.toEntity(dto);
        return mapper.toDTO(update(id, entity));
    }

    public FacturaResponseDTO findByIdDTO(Long id) {
        return mapper.toDTO(findById(id));
    }

    public java.util.List<FacturaResponseDTO> findAllDTO() {
        return mapper.toDTOList(findAll());
    }
}
