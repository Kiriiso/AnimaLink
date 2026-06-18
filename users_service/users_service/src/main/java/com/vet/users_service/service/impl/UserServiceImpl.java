package com.vet.users_service.service.impl;

import com.vet.users_service.dto.UserRequestDTO;
import com.vet.users_service.dto.UserResponseDTO;
import com.vet.users_service.exception.ResourceNotFoundException;
import com.vet.users_service.mapper.UserMapper;
import com.vet.users_service.model.User;
import com.vet.users_service.repository.UserRepository;
import com.vet.users_service.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public User save(User entity) {
        return repository.save(entity);
    }

    @Override
    public User update(Long id, User entity) {
        User existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));        existing.setUsername(entity.getUsername());
        existing.setEmail(entity.getEmail());
        existing.setFirstName(entity.getFirstName());
        existing.setLastName(entity.getLastName());
        existing.setCreatedAt(entity.getCreatedAt());
        return repository.save(existing);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    @Override
    public java.util.List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        repository.deleteById(id);
    }

    public UserResponseDTO saveDTO(UserRequestDTO dto) {
        User entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public UserResponseDTO updateDTO(Long id, UserRequestDTO dto) {
        User entity = mapper.toEntity(dto);
        return mapper.toDTO(update(id, entity));
    }

    public UserResponseDTO findByIdDTO(Long id) {
        return mapper.toDTO(findById(id));
    }

    public java.util.List<UserResponseDTO> findAllDTO() {
        return mapper.toDTOList(findAll());
    }
}
