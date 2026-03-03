package com.rev.app.controller;

import com.rev.app.entity.Department;
import com.rev.app.entity.Designation;
import com.rev.app.entity.Employee;
import com.rev.app.dto.AnnouncementDto;
import com.rev.app.service.ConfigService;
import com.rev.app.service.AnnouncementService;
import com.rev.app.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
public class AdminConfigController {

    @Autowired
    private ConfigService configService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private EmployeeService employeeService;

    // Departments
    @GetMapping("/admin/config/departments")
    public String manageDepartments(Model model) {
        model.addAttribute("departments", configService.getAllDepartments());
        model.addAttribute("newDept", new Department());
        return "admin/config/departments";
    }

    @PostMapping("/admin/config/departments/save")
    public String saveDepartment(@ModelAttribute("newDept") Department department, Principal principal) {
        configService.saveDepartment(department);
        if (principal != null) {
            configService.logActivity("MANAGE_DEPARTMENT", principal.getName(),
                    "Saved department: " + department.getDepartmentName());
        }
        return "redirect:/admin/config/departments";
    }

    @GetMapping("/admin/config/departments/delete/{id}")
    public String deleteDepartment(@PathVariable("id") Long id, Principal principal) {
        configService.deleteDepartment(id);
        if (principal != null) {
            configService.logActivity("DELETE_DEPARTMENT", principal.getName(), "Deleted department ID: " + id);
        }
        return "redirect:/admin/config/departments";
    }

    // Designations
    @GetMapping("/admin/config/designations")
    public String manageDesignations(Model model) {
        model.addAttribute("designations", configService.getAllDesignations());
        model.addAttribute("newDesignation", new Designation());
        return "admin/config/designations";
    }

    @PostMapping("/admin/config/designations/save")
    public String saveDesignation(@ModelAttribute("newDesignation") Designation designation, Principal principal) {
        configService.saveDesignation(designation);
        if (principal != null) {
            configService.logActivity("MANAGE_DESIGNATION", principal.getName(),
                    "Saved designation: " + designation.getDesignationName());
        }
        return "redirect:/admin/config/designations";
    }

    @GetMapping("/admin/config/designations/delete/{id}")
    public String deleteDesignation(@PathVariable("id") Long id, Principal principal) {
        configService.deleteDesignation(id);
        if (principal != null) {
            configService.logActivity("DELETE_DESIGNATION", principal.getName(), "Deleted designation ID: " + id);
        }
        return "redirect:/admin/config/designations";
    }

    // Announcements
    @GetMapping("/admin/config/announcements")
    public String manageAnnouncements(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        org.springframework.data.domain.Page<AnnouncementDto> announcementsPage = announcementService
                .getAllAnnouncements(page, size);
        model.addAttribute("announcements", announcementsPage.getContent());
        model.addAttribute("page", announcementsPage);
        model.addAttribute("newAnnouncement", new AnnouncementDto());
        return "admin/config/announcements";
    }

    @PostMapping("/admin/config/announcements/save")
    public String saveAnnouncement(@ModelAttribute("newAnnouncement") AnnouncementDto dto, Principal principal) {
        if (dto.getAnnouncementId() != null) {
            announcementService.updateAnnouncement(dto);
            if (principal != null) {
                configService.logActivity("UPDATE_ANNOUNCEMENT", principal.getName(),
                        "Updated announcement: " + dto.getTitle());
            }
        } else {
            if (principal != null) {
                dto.setCreatedBy(principal.getName());
                announcementService.createAnnouncement(dto);
                configService.logActivity("CREATE_ANNOUNCEMENT", principal.getName(),
                        "Created announcement: " + dto.getTitle());
            }
        }
        return "redirect:/admin/config/announcements";
    }

    @GetMapping("/admin/config/announcements/delete/{id}")
    public String deleteAnnouncement(@PathVariable("id") Long id, Principal principal) {
        announcementService.deleteAnnouncement(id);
        if (principal != null) {
            configService.logActivity("DELETE_ANNOUNCEMENT", principal.getName(), "Deleted announcement ID: " + id);
        }
        return "redirect:/admin/config/announcements";
    }

    // Activity Logs
    @GetMapping("/admin/config/logs")
    public String viewLogs(Model model) {
        model.addAttribute("logs", configService.getAllLogs());
        return "admin/config/logs";
    }
}
