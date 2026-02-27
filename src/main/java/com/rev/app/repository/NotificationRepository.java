package com.rev.app.repository;

import com.rev.app.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByEmployeeEmployeeId(Long employeeId);
}