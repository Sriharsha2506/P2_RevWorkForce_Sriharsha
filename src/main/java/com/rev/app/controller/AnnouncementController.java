package com.rev.app.controller;

import com.rev.app.entity.Announcement;
import com.rev.app.service.AnnouncementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {

    private final AnnouncementService service;

    public AnnouncementController(AnnouncementService service) {
        this.service = service;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("announcement", new Announcement());
        return "announcement-form";
    }

    @PostMapping("/save")
    public String saveAnnouncement(@ModelAttribute Announcement announcement) {
        service.createAnnouncement(announcement);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/all")
    public String viewAnnouncements(Model model) {
        model.addAttribute("announcements", service.getAllAnnouncements());
        return "announcement-list";
    }
}