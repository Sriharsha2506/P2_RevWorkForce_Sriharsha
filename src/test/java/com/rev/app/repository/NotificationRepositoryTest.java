package com.rev.app.repository;

import com.rev.app.entity.Notification;
import com.rev.app.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUserIdOrderByCreatedAtDesc_ReturnsNotifications() {
        User user = new User();
        user.setEmail("notify@rev.com");
        user.setPassword("pwd");
        user.setRole(User.Role.ROLE_EMPLOYEE);
        user.setIsActive(1);
        userRepository.save(user);

        Notification n1 = new Notification();
        n1.setUser(user);
        n1.setMessage("Leave approved");
        n1.setType("LEAVE_APPROVED");
        n1.setIsRead(0);
        notificationRepository.save(n1);

        Notification n2 = new Notification();
        n2.setUser(user);
        n2.setMessage("New announcement");
        n2.setType("ANNOUNCEMENT");
        n2.setIsRead(0);
        notificationRepository.save(n2);

        List<Notification> result = notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(user.getUserId());

        assertEquals(2, result.size());
    }

    @Test
    void testSaveNotification_PersistsRecord() {
        User user = new User();
        user.setEmail("notify2@rev.com");
        user.setPassword("pwd");
        user.setRole(User.Role.ROLE_EMPLOYEE);
        user.setIsActive(1);
        userRepository.save(user);

        Notification n = new Notification();
        n.setUser(user);
        n.setMessage("Welcome!");
        n.setType("SYSTEM");
        n.setIsRead(0);
        Notification saved = notificationRepository.save(n);

        assertNotNull(saved.getNotificationId());
        assertEquals("Welcome!", saved.getMessage());
    }
}
