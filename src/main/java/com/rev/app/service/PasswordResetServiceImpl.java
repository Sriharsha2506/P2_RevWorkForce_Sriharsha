package com.rev.app.service;

import com.rev.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void processForgotPassword(String email) {
        userRepository.findByEmailIgnoreCase(email).ifPresent(user -> {
            String resetLink = "http://localhost:8082/reset-password?email=" + email;

            // Console logging for easy testing without SMTP
            System.out.println("\n==================================================");
            System.out.println("DEBUG: Password Reset Link for: " + email);
            System.out.println("LINK: " + resetLink);
            System.out.println("==================================================\n");

            try {
                emailService.sendPasswordResetEmail(email, resetLink);
            } catch (Exception e) {
                System.err.println(
                        "Note: Real email could not be sent (SMTP not configured). Using Console link instead.");
            }
        });
    }

    @Override
    @Transactional
    public void resetPassword(String email, String newPassword) {
        userRepository.findByEmailIgnoreCase(email).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        });
    }
}
