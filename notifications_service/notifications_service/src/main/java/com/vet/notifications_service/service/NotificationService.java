package com.vet.notifications_service.service;

import com.vet.notifications_service.model.Notification;
import java.util.List;

public interface NotificationService {

    Notification save(Notification entity);

    Notification update(Long id, Notification entity);

    Notification findById(Long id);

    List<Notification> findAll();

    void deleteById(Long id);
}
