package com.rev.app.rest;

import com.rev.app.dto.NotificationDto;
import com.rev.app.entity.User.Role;
import com.rev.app.entity.User;
import com.rev.app.service.NotificationService;
import com.rev.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NotificationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private UserService userService;

    private NotificationDto notificationDto;

    @BeforeEach
    void setUp() {
        notificationDto = new NotificationDto();
        notificationDto.setNotificationId(1L);
        notificationDto.setMessage("Test notification");
        notificationDto.setIsRead(0);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void testGetNotifications() throws Exception {
        when(notificationService.getUserNotifications(eq(1L)))
                .thenReturn(Collections.singletonList(notificationDto));

        mockMvc.perform(get("/api/notifications/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notificationId").value(1))
                .andExpect(jsonPath("$[0].message").value("Test notification"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllNotifications() throws Exception {
        when(notificationService.getAllNotifications())
                .thenReturn(Collections.singletonList(notificationDto));

        mockMvc.perform(get("/api/notifications/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notificationId").value(1))
                .andExpect(jsonPath("$[0].message").value("Test notification"));
    }

    @Test
    @WithMockUser(username = "bob@example.com", roles = "EMPLOYEE")
    void testGetCurrentUser() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("bob@example.com");
        user.setRole(Role.ROLE_EMPLOYEE);

        when(userService.findByEmail("bob@example.com")).thenReturn(Optional.of(user));
        when(notificationService.getUserNotifications(1L)).thenReturn(Collections.singletonList(notificationDto));

        mockMvc.perform(get("/api/notifications/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("bob@example.com"))
                .andExpect(jsonPath("$.userId").value(1));
    }
}
