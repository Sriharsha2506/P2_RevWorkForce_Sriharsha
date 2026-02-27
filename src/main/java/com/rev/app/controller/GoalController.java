package com.rev.app.controller;

import com.rev.app.entity.Employee;
import com.rev.app.entity.Goal;
import com.rev.app.service.GoalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    // ===============================
    // SHOW ADD GOAL FORM
    // ===============================
    @GetMapping("/form")
    public String showGoalForm(HttpSession session, Model model) {

        Employee user = (Employee) session.getAttribute("loggedUser");

        if (user == null ||
                !"EMPLOYEE".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("goal", new Goal());
        return "goal-form";
    }

    // ===============================
    // SAVE GOAL
    // ===============================
    @PostMapping("/save")
    public String saveGoal(@ModelAttribute Goal goal,
                           HttpSession session) {

        Employee user = (Employee) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        goal.setEmployee(user);
        goal.setStatus("PENDING");

        goalService.saveGoal(goal);

        return "redirect:/goals/my";
    }

    // ===============================
    // VIEW MY GOALS
    // ===============================
    @GetMapping("/my")
    public String viewMyGoals(HttpSession session, Model model) {

        Employee user = (Employee) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("goals",
                goalService.getGoalsByEmployee(user.getEmployeeId()));

        return "goal-list";
    }
}