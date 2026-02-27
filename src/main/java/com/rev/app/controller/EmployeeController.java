package com.rev.app.controller;

import com.rev.app.entity.Employee;
import com.rev.app.service.AnnouncementService;
import com.rev.app.service.EmployeeService;
import com.rev.app.service.PerformanceReviewService;
import com.rev.app.service.NotificationService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;
    private final PerformanceReviewService performanceReviewService;
    private final AnnouncementService announcementService;
    private final NotificationService notificationService;

    public EmployeeController(EmployeeService service,
                              PerformanceReviewService performanceReviewService,
                              AnnouncementService announcementService,
                              NotificationService notificationService) {
        this.service = service;
        this.performanceReviewService = performanceReviewService;
        this.announcementService = announcementService;
        this.notificationService = notificationService;
    }

    // ===============================
    // VIEW ALL EMPLOYEES (ADMIN ONLY)
    // ===============================
    @GetMapping("/list")
    public String listEmployees(HttpSession session, Model model) {

        Employee loggedUser = (Employee) session.getAttribute("loggedUser");

        if (loggedUser == null ||
                !"ADMIN".equalsIgnoreCase(loggedUser.getRole())) {
            return "redirect:/login";
        }

        List<Employee> employees = service.getAllEmployees();
        model.addAttribute("employees", employees);

        return "employee-list";
    }

    // ===============================
    // SHOW ADD FORM (ADMIN ONLY)
    // ===============================
    @GetMapping("/form")
    public String showForm(HttpSession session, Model model) {

        Employee loggedUser = (Employee) session.getAttribute("loggedUser");

        if (loggedUser == null ||
                !"ADMIN".equalsIgnoreCase(loggedUser.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("employee", new Employee());
        return "employee-form";
    }

    // ===============================
    // SAVE EMPLOYEE (ADMIN ONLY)
    // ===============================
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee,
                               HttpSession session) {

        Employee loggedUser = (Employee) session.getAttribute("loggedUser");

        if (loggedUser == null ||
                !"ADMIN".equalsIgnoreCase(loggedUser.getRole())) {
            return "redirect:/login";
        }

        if (employee.getStatus() == null) {
            employee.setStatus("ACTIVE");
        }

        service.saveEmployee(employee);

        return "redirect:/employees/list";
    }

    // ===============================
    // EMPLOYEE DASHBOARD
    // ===============================
    @GetMapping("/dashboard")
    public String employeeDashboard(HttpSession session, Model model) {

        Employee loggedUser = (Employee) session.getAttribute("loggedUser");

        if (loggedUser == null ||
                !"EMPLOYEE".equalsIgnoreCase(loggedUser.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("user", loggedUser);

        // Announcements
        model.addAttribute("announcements",
                announcementService.getAllAnnouncements());

        // Notifications (NEW)
        model.addAttribute("notifications",
                notificationService.getNotificationsByEmployee(
                        loggedUser.getEmployeeId()));

        return "employee-dashboard";
    }

    // ===============================
    // VIEW MY REVIEWS
    // ===============================
    @GetMapping("/reviews")
    public String viewMyReviews(HttpSession session, Model model) {

        Employee loggedUser = (Employee) session.getAttribute("loggedUser");

        if (loggedUser == null ||
                !"EMPLOYEE".equalsIgnoreCase(loggedUser.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("reviews",
                performanceReviewService
                        .getReviewsByEmployee(
                                loggedUser.getEmployeeId()));

        return "review-list";
    }
}