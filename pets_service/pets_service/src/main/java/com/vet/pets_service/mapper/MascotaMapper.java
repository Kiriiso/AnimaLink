package com.vet.pets_service.mapper;

import com.vet.pets_service.dto.MascotaRequestDTO;
import com.vet.pets_service.dto.MascotaResponseDTO;
import com.vet.pets_service.model.Mascota;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MascotaMapper {

    public Mascota toEntity(MascotaRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Mascota entity = new Mascota();
        entity.setId(dto.getId());        entity.setNombre(dto.getNombre());
        entity.setEspecie(dto.getEspecie());
        entity.setRaza(dto.getRaza());
        entity.setClienteId(dto.getClienteId());
        return entity;
    }

    public MascotaResponseDTO toDTO(Mascota entity) {
        if (entity == null) {
            return null;
        }
        MascotaResponseDTO dto = new MascotaResponseDTO();
        dto.setId(entity.getId());        dto.setNombre(entity.getNombre());
        dto.setEspecie(entity.getEspecie());
        dto.setRaza(entity.getRaza());
        dto.setClienteId(entity.getClienteId());
        return dto;
    }

    public java.util.List<MascotaResponseDTO> toDTOList(java.util.List<Mascota> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
