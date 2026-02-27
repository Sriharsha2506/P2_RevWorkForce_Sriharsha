package com.rev.app.controller;

import com.rev.app.entity.Employee;
import com.rev.app.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final EmployeeService employeeService;

    public AuthController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {

        Employee user = employeeService.login(email, password);

        if (user == null) {
            return "login";
        }

        session.setAttribute("loggedUser", user);

        if ("ADMIN".equals(user.getRole())) {
            return "redirect:/admin/dashboard";
        } else if ("MANAGER".equals(user.getRole())) {
            return "redirect:/manager/dashboard";
        } else {
            return "redirect:/employee/dashboard";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}