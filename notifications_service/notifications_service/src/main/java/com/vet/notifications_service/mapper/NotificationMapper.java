package com.vet.notifications_service.mapper;

import com.vet.notifications_service.dto.NotificationRequestDTO;
import com.vet.notifications_service.dto.NotificationResponseDTO;
import com.vet.notifications_service.model.Notification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    public Notification toEntity(NotificationRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Notification entity = new Notification();
        entity.setId(dto.getId());        entity.setRecipient(dto.getRecipient());
        entity.setMessage(dto.getMessage());
        entity.setSent(dto.isSent());
        entity.setSentAt(dto.getSentAt());
        return entity;
    }

    public NotificationResponseDTO toDTO(Notification entity) {
        if (entity == null) {
            return null;
        }
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(entity.getId());        dto.setRecipient(entity.getRecipient());
        dto.setMessage(entity.getMessage());
        dto.setSent(entity.isSent());
        dto.setSentAt(entity.getSentAt());
        return dto;
    }

    public java.util.List<NotificationResponseDTO> toDTOList(java.util.List<Notification> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
