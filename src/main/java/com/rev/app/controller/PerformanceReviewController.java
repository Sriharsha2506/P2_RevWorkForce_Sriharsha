package com.rev.app.controller;

import com.rev.app.entity.Employee;
import com.rev.app.entity.PerformanceReview;
import com.rev.app.service.PerformanceReviewService;
import com.rev.app.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/performance-reviews")
public class PerformanceReviewController {

    @Autowired
    private PerformanceReviewService reviewService;

    @Autowired
    private EmployeeService employeeService;

    // ===============================
    // SHOW REVIEW FORM (Manager)
    // ===============================
    @GetMapping("/manager/review/{employeeId}")
    public String showReviewForm(@PathVariable Long employeeId, Model model) {

        PerformanceReview review = new PerformanceReview();

        Employee employee = employeeService.getEmployeeById(employeeId);
        review.setEmployee(employee);

        model.addAttribute("review", review);

        return "manager-review-form";
    }

    // ===============================
    // SUBMIT REVIEW FROM UI
    // ===============================
    @PostMapping("/manager/submit")
    public String submitReviewFromUI(@ModelAttribute PerformanceReview review) {

        reviewService.submitReview(review);

        return "redirect:/manager/dashboard";
    }

    // ===============================
    // VIEW REVIEWS OF EMPLOYEE
    // ===============================
    @GetMapping("/employee/{id}")
    public String viewEmployeeReviews(@PathVariable Long id, Model model) {

        List<PerformanceReview> reviews = reviewService.getReviewsByEmployee(id);
        model.addAttribute("reviews", reviews);

        return "employee-reviews";
    }
}