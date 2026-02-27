package com.rev.app.service;

import com.rev.app.entity.Notification;
import com.rev.app.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    public NotificationServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification save(Notification notification) {
        notification.setCreatedAt(LocalDateTime.now());
        return repository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsByEmployee(Long employeeId) {
        return repository.findByEmployeeEmployeeId(employeeId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);
        repository.save(notification);
    }
}