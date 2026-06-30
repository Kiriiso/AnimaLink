package com.vet.cliente_service.service.impl;

import com.vet.cliente_service.dto.ClienteRequestDTO;
import com.vet.cliente_service.dto.ClienteResponseDTO;
import com.vet.cliente_service.exception.ResourceNotFoundException;
import com.vet.cliente_service.mapper.ClienteMapper;
import com.vet.cliente_service.model.Cliente;
import com.vet.cliente_service.repository.ClienteRepository;
import com.vet.cliente_service.service.ClienteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    public ClienteServiceImpl(ClienteRepository repository, ClienteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Cliente save(Cliente entity) {
        return repository.save(entity);
    }

    @Override
    public Cliente update(Long id, Cliente entity) {
        Cliente existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente not found with id " + id));        existing.setNombre(entity.getNombre());
        existing.setEmail(entity.getEmail());
        existing.setTelefono(entity.getTelefono());
        existing.setDireccion(entity.getDireccion());
        return repository.save(existing);
    }

    @Override
    public Cliente findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente not found with id " + id));
    }

    @Override
    public java.util.List<Cliente> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente not found with id " + id);
        }
        repository.deleteById(id);
    }

    public ClienteResponseDTO saveDTO(ClienteRequestDTO dto) {
        Cliente entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public ClienteResponseDTO updateDTO(Long id, ClienteRequestDTO dto) {
        Cliente entity = mapper.toEntity(dto);
        return mapper.toDTO(update(id, entity));
    }

    public ClienteResponseDTO findByIdDTO(Long id) {
        return mapper.toDTO(findById(id));
    }

    public java.util.List<ClienteResponseDTO> findAllDTO() {
        return mapper.toDTOList(findAll());
    }
}
