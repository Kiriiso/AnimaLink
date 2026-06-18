package com.vet.auth_service.mapper;

import com.vet.auth_service.dto.AuthUserRequestDTO;
import com.vet.auth_service.dto.AuthUserResponseDTO;
import com.vet.auth_service.model.AuthUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthUserMapper {

    public AuthUser toEntity(AuthUserRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        AuthUser entity = new AuthUser();
        entity.setId(dto.getId());        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        return entity;
    }

    public AuthUserResponseDTO toDTO(AuthUser entity) {
        if (entity == null) {
            return null;
        }
        AuthUserResponseDTO dto = new AuthUserResponseDTO();
        dto.setId(entity.getId());        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        return dto;
    }

    public java.util.List<AuthUserResponseDTO> toDTOList(java.util.List<AuthUser> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
