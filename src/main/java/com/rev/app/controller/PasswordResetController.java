package com.rev.app.controller;

import com.rev.app.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        passwordResetService.processForgotPassword(email);
        redirectAttributes.addFlashAttribute("message",
                "If an account exists for " + email + ", a reset link has been sent.");
        return "redirect:/forgot-password?success";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/reset-password?email=" + email;
        }
        passwordResetService.resetPassword(email, password);
        redirectAttributes.addFlashAttribute("message", "Your password has been reset successfully.");
        return "redirect:/login";
    }
}
