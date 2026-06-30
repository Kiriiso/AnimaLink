package com.vet.cliente_service.mapper;

import com.vet.cliente_service.dto.ClienteRequestDTO;
import com.vet.cliente_service.dto.ClienteResponseDTO;
import com.vet.cliente_service.model.Cliente;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Cliente entity = new Cliente();
        entity.setId(dto.getId());        entity.setNombre(dto.getNombre());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());
        entity.setDireccion(dto.getDireccion());
        return entity;
    }

    public ClienteResponseDTO toDTO(Cliente entity) {
        if (entity == null) {
            return null;
        }
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(entity.getId());        dto.setNombre(entity.getNombre());
        dto.setEmail(entity.getEmail());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(entity.getDireccion());
        return dto;
    }

    public java.util.List<ClienteResponseDTO> toDTOList(java.util.List<Cliente> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
