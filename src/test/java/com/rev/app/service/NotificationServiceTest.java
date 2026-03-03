package com.rev.app.service;

import com.rev.app.dto.NotificationDto;
import com.rev.app.entity.Notification;
import com.rev.app.entity.User;
import com.rev.app.mapper.DTOMapper;
import com.rev.app.repository.NotificationRepository;
import com.rev.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificationService, "dtoMapper", new DTOMapper());
    }

    @Test
    void testSendNotification_ValidUser_SavesNotification() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(new Notification());

        notificationService.sendNotification(1L, "Your leave is approved", "LEAVE_APPROVED");

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testSendNotification_UserNotFound_SkipsSave() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        notificationService.sendNotification(99L, "Test message", "TEST");

        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testGetUserNotifications_ReturnsListForUser() {
        User user = new User();
        user.setUserId(2L);

        Notification n1 = new Notification();
        n1.setUser(user);
        n1.setMessage("Msg1");
        n1.setType("TYPE_A");

        Notification n2 = new Notification();
        n2.setUser(user);
        n2.setMessage("Msg2");
        n2.setType("TYPE_B");

        when(notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(2L))
                .thenReturn(Arrays.asList(n1, n2));

        List<NotificationDto> result = notificationService.getUserNotifications(2L);

        assertEquals(2, result.size());
    }

    @Test
    void testMarkAsRead_ExistingNotification_SetsRead() {
        Notification n = new Notification();
        n.setIsRead(0);

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(n));
        when(notificationRepository.save(any())).thenReturn(n);

        notificationService.markAsRead(1L);

        verify(notificationRepository).save(argThat(notif -> notif.getIsRead() == 1));
    }
}
