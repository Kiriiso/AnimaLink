package com.vet.users_service.mapper;

import com.vet.users_service.dto.UserRequestDTO;
import com.vet.users_service.dto.UserResponseDTO;
import com.vet.users_service.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        User entity = new User();
        entity.setId(dto.getId());        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public UserResponseDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(entity.getId());        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public java.util.List<UserResponseDTO> toDTOList(java.util.List<User> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
