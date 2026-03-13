package com.rev.app.service;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String resetLink);
}
