package com.vet.notifications_service.service.impl;

import com.vet.notifications_service.dto.NotificationRequestDTO;
import com.vet.notifications_service.dto.NotificationResponseDTO;
import com.vet.notifications_service.exception.ResourceNotFoundException;
import com.vet.notifications_service.mapper.NotificationMapper;
import com.vet.notifications_service.model.Notification;
import com.vet.notifications_service.repository.NotificationRepository;
import com.vet.notifications_service.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public NotificationServiceImpl(NotificationRepository repository, NotificationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Notification save(Notification entity) {
        return repository.save(entity);
    }

    @Override
    public Notification update(Long id, Notification entity) {
        Notification existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification not found with id " + id));        existing.setRecipient(entity.getRecipient());
        existing.setMessage(entity.getMessage());
        existing.setSent(entity.isSent());
        existing.setSentAt(entity.getSentAt());
        return repository.save(existing);
    }

    @Override
    public Notification findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification not found with id " + id));
    }

    @Override
    public java.util.List<Notification> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with id " + id);
        }
        repository.deleteById(id);
    }

    public NotificationResponseDTO saveDTO(NotificationRequestDTO dto) {
        Notification entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public NotificationResponseDTO updateDTO(Long id, NotificationRequestDTO dto) {
        Notification entity = mapper.toEntity(dto);
        return mapper.toDTO(update(id, entity));
    }

    public NotificationResponseDTO findByIdDTO(Long id) {
        return mapper.toDTO(findById(id));
    }

    public java.util.List<NotificationResponseDTO> findAllDTO() {
        return mapper.toDTOList(findAll());
    }
}
