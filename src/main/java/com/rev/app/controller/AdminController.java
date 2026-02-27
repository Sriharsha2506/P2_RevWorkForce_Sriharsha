package com.rev.app.controller;

import com.rev.app.entity.Employee;
import com.rev.app.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final EmployeeService employeeService;

    public AdminController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // ================= VIEW ALL EMPLOYEES =================
    @GetMapping("/admin/employees")
    public String viewEmployees(HttpSession session, Model model) {

        Employee user = (Employee) session.getAttribute("loggedUser");

        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("employees",
                employeeService.getAllEmployees());

        return "employee-list";
    }
}