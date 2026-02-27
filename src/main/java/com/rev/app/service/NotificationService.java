package com.rev.app.service;

import com.rev.app.entity.Notification;
import java.util.List;

public interface NotificationService {

    Notification save(Notification notification);

    List<Notification> getNotificationsByEmployee(Long employeeId);

    void markAsRead(Long notificationId);
}