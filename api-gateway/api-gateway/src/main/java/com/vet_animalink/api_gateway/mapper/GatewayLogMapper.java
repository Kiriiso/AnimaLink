package com.vet_animalink.api_gateway.mapper;

import com.vet_animalink.api_gateway.dto.GatewayLogRequestDTO;
import com.vet_animalink.api_gateway.dto.GatewayLogResponseDTO;
import com.vet_animalink.api_gateway.model.GatewayLog;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GatewayLogMapper {

    public GatewayLog toEntity(GatewayLogRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        GatewayLog entity = new GatewayLog();
        entity.setId(dto.getId());        entity.setPath(dto.getPath());
        entity.setMethod(dto.getMethod());
        entity.setStatus(dto.getStatus());
        entity.setTimestamp(dto.getTimestamp());
        return entity;
    }

    public GatewayLogResponseDTO toDTO(GatewayLog entity) {
        if (entity == null) {
            return null;
        }
        GatewayLogResponseDTO dto = new GatewayLogResponseDTO();
        dto.setId(entity.getId());        dto.setPath(entity.getPath());
        dto.setMethod(entity.getMethod());
        dto.setStatus(entity.getStatus());
        dto.setTimestamp(entity.getTimestamp());
        return dto;
    }

    public java.util.List<GatewayLogResponseDTO> toDTOList(java.util.List<GatewayLog> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
