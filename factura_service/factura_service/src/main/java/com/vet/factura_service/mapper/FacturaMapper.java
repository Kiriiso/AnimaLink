package com.vet.factura_service.mapper;

import com.vet.factura_service.dto.FacturaRequestDTO;
import com.vet.factura_service.dto.FacturaResponseDTO;
import com.vet.factura_service.model.Factura;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FacturaMapper {

    public Factura toEntity(FacturaRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Factura entity = new Factura();
        entity.setId(dto.getId());        entity.setClienteId(dto.getClienteId());
        entity.setAmount(dto.getAmount());
        entity.setDescription(dto.getDescription());
        entity.setPaid(dto.isPaid());
        entity.setIssueDate(dto.getIssueDate());
        return entity;
    }

    public FacturaResponseDTO toDTO(Factura entity) {
        if (entity == null) {
            return null;
        }
        FacturaResponseDTO dto = new FacturaResponseDTO();
        dto.setId(entity.getId());        dto.setClienteId(entity.getClienteId());
        dto.setAmount(entity.getAmount());
        dto.setDescription(entity.getDescription());
        dto.setPaid(entity.isPaid());
        dto.setIssueDate(entity.getIssueDate());
        return dto;
    }

    public java.util.List<FacturaResponseDTO> toDTOList(java.util.List<Factura> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
