package com.vet.auth_service.service.impl;

import com.vet.auth_service.dto.AuthUserRequestDTO;
import com.vet.auth_service.dto.AuthUserResponseDTO;
import com.vet.auth_service.exception.ResourceNotFoundException;
import com.vet.auth_service.mapper.AuthUserMapper;
import com.vet.auth_service.model.AuthUser;
import com.vet.auth_service.repository.AuthUserRepository;
import com.vet.auth_service.service.AuthUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository repository;
    private final AuthUserMapper mapper;

    public AuthUserServiceImpl(AuthUserRepository repository, AuthUserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public AuthUser save(AuthUser entity) {
        return repository.save(entity);
    }

    @Override
    public AuthUser update(Long id, AuthUser entity) {
        AuthUser existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("AuthUser not found with id " + id));        existing.setUsername(entity.getUsername());
        existing.setPassword(entity.getPassword());
        existing.setEmail(entity.getEmail());
        existing.setRole(entity.getRole());
        return repository.save(existing);
    }

    @Override
    public AuthUser findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("AuthUser not found with id " + id));
    }

    @Override
    public java.util.List<AuthUser> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("AuthUser not found with id " + id);
        }
        repository.deleteById(id);
    }

    public AuthUserResponseDTO saveDTO(AuthUserRequestDTO dto) {
        AuthUser entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public AuthUserResponseDTO updateDTO(Long id, AuthUserRequestDTO dto) {
        AuthUser entity = mapper.toEntity(dto);
        return mapper.toDTO(update(id, entity));
    }

    public AuthUserResponseDTO findByIdDTO(Long id) {
        return mapper.toDTO(findById(id));
    }

    public java.util.List<AuthUserResponseDTO> findAllDTO() {
        return mapper.toDTOList(findAll());
    }
}
