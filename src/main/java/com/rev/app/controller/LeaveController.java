package com.rev.app.controller;

import com.rev.app.entity.Employee;
import com.rev.app.entity.Leave;
import com.rev.app.service.LeaveService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // ================= APPLY =================

    @GetMapping("/apply")
    public String showApplyForm(HttpSession session, Model model) {

        Employee loggedUser = (Employee) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("leave", new Leave());
        return "leave-form";
    }

    @PostMapping("/apply")
    public String applyLeave(@ModelAttribute Leave leave,
                             HttpSession session) {

        Employee loggedUser = (Employee) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            return "redirect:/login";
        }

        leave.setEmployee(loggedUser);
        leaveService.applyLeave(leave);

        return "redirect:/leaves/my";
    }

    // ================= VIEW OWN =================

    @GetMapping("/my")
    public String myLeaves(HttpSession session, Model model) {

        Employee loggedUser = (Employee) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("leaves",
                leaveService.getEmployeeLeaves(loggedUser));

        model.addAttribute("role", loggedUser.getRole());

        return "leave-list";
    }

    // ================= PENDING (Manager/Admin) =================

    @GetMapping("/pending")
    public String pendingLeaves(HttpSession session, Model model) {

        Employee user = (Employee) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        if (!"MANAGER".equals(user.getRole())
                && !"ADMIN".equals(user.getRole())) {
            return "redirect:/employee/dashboard";
        }

        model.addAttribute("leaves",
                leaveService.getPendingLeaves());

        model.addAttribute("role", user.getRole());

        return "leave-list";
    }

    // ================= APPROVE =================

    @GetMapping("/approve/{id}")
    public String approve(@PathVariable Long id,
                          HttpSession session) {

        Employee user = (Employee) session.getAttribute("loggedUser");

        if (user == null ||
                (!"MANAGER".equals(user.getRole())
                        && !"ADMIN".equals(user.getRole()))) {

            return "redirect:/login";
        }

        leaveService.approveLeave(id);
        return "redirect:/leaves/pending";
    }

    // ================= REJECT =================

    @GetMapping("/reject/{id}")
    public String reject(@PathVariable Long id,
                         HttpSession session) {

        Employee user = (Employee) session.getAttribute("loggedUser");

        if (user == null ||
                (!"MANAGER".equals(user.getRole())
                        && !"ADMIN".equals(user.getRole()))) {

            return "redirect:/login";
        }

        leaveService.rejectLeave(id);
        return "redirect:/leaves/pending";
    }
}