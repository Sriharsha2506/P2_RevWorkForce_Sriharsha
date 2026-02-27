package com.rev.app.controller;

import com.rev.app.entity.Employee;
import com.rev.app.entity.PerformanceReview;
import com.rev.app.service.AnnouncementService;
import com.rev.app.service.EmployeeService;
import com.rev.app.service.LeaveService;
import com.rev.app.service.PerformanceReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.YearMonth;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final EmployeeService employeeService;
    private final LeaveService leaveService;
    private final AnnouncementService announcementService;
    private final PerformanceReviewService performanceReviewService;

    public ManagerController(EmployeeService employeeService,
                             LeaveService leaveService,
                             AnnouncementService announcementService,
                             PerformanceReviewService performanceReviewService) {
        this.employeeService = employeeService;
        this.leaveService = leaveService;
        this.announcementService = announcementService;
        this.performanceReviewService = performanceReviewService;
    }

    // ===============================
    // MANAGER DASHBOARD
    // ===============================


    // ===============================
    // VIEW PENDING LEAVES
    // ===============================
    @GetMapping("/leaves/pending")
    public String viewPendingLeaves(Model model) {

        model.addAttribute("leaves", leaveService.getPendingLeaves());

        return "pending-leaves";
    }

    // ===============================
    // SHOW REVIEW FORM
    // ===============================
    @GetMapping("/review/{employeeId}")
    public String showReviewForm(@PathVariable Long employeeId, Model model) {

        PerformanceReview review = new PerformanceReview();
        Employee employee = employeeService.getEmployeeById(employeeId);

        review.setEmployee(employee);

        review.setReviewPeriod(YearMonth.now().toString());

        model.addAttribute("review", review);

        return "review-form";
    }

    // ===============================
    // SAVE REVIEW
    // ===============================
    @PostMapping("/review/save")
    public String saveReview(@ModelAttribute PerformanceReview review) {

        performanceReviewService.submitReview(review);

        return "redirect:/manager/dashboard";
    }
}