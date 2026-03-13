package com.rev.app.service;

public interface PasswordResetService {
    void processForgotPassword(String email);

    void resetPassword(String email, String newPassword);
}
