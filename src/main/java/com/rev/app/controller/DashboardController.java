package com.rev.app.controller;

import com.rev.app.entity.Employee;
import com.rev.app.repository.AnnouncementRepository;
import com.rev.app.repository.EmployeeRepository;
import com.rev.app.repository.LeaveRepository;
import com.rev.app.service.AnnouncementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final AnnouncementService announcementService;
    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;
    private final AnnouncementRepository announcementRepository;

    public DashboardController(AnnouncementService announcementService,
                               EmployeeRepository employeeRepository,
                               LeaveRepository leaveRepository,
                               AnnouncementRepository announcementRepository) {
        this.announcementService = announcementService;
        this.employeeRepository = employeeRepository;
        this.leaveRepository = leaveRepository;
        this.announcementRepository = announcementRepository;
    }

    // ================= ADMIN DASHBOARD =================
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {

        Employee user = (Employee) session.getAttribute("loggedUser");

        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("totalEmployees",
                employeeRepository.countByRole("EMPLOYEE"));

        model.addAttribute("totalManagers",
                employeeRepository.countByRole("MANAGER"));

        model.addAttribute("pendingLeaves",
                leaveRepository.countByStatus("PENDING"));

        model.addAttribute("approvedLeaves",
                leaveRepository.countByStatus("APPROVED"));

        model.addAttribute("totalAnnouncements",
                announcementRepository.count());

        return "admin-dashboard";
    }

    // ================= MANAGER DASHBOARD =================
    @GetMapping("/manager/dashboard")
    public String managerDashboard(HttpSession session, Model model) {

        Employee user = (Employee) session.getAttribute("loggedUser");

        if (user == null || !"MANAGER".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("announcements",
                announcementService.getAllAnnouncements());

        model.addAttribute("pendingLeaves",
                leaveRepository.countByStatus("PENDING"));

        // 🔥 ADD THIS LINE
        model.addAttribute("employees",
                employeeRepository.findByRole("EMPLOYEE"));

        return "manager-dashboard";
    }
    // ================= EMPLOYEE DASHBOARD =================
    @GetMapping("/employee/dashboard")
    public String employeeDashboard(HttpSession session, Model model) {

        Employee user = (Employee) session.getAttribute("loggedUser");

        if (user == null || !"EMPLOYEE".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("announcements",
                announcementService.getAllAnnouncements());

        model.addAttribute("approvedLeaves",
                leaveRepository.countByStatus("APPROVED"));

        return "employee-dashboard";
    }
}