package com.rev.app.service;

import com.rev.app.repository.UserRepository;
import com.rev.app.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class PasswordResetServiceTest {

    @Autowired
    private PasswordResetService passwordResetService;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void testResetLinkIsSent() {
        String email = "employee@rev.com";
        User mockUser = new User();
        mockUser.setEmail(email);

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(mockUser));

        passwordResetService.processForgotPassword(email);

        // Verifies that the email service was actually invoked with the correct link
        // format
        verify(emailService).sendPasswordResetEmail(eq(email), contains("/reset-password?email=" + email));
    }
}
