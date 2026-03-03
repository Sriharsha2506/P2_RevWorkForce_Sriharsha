package com.rev.app.controller;

import com.rev.app.service.ConfigService;
import com.rev.app.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/reports")
public class AdminReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ConfigService configService; // To get departments for filtering

    @GetMapping("/employees")
    public String employeeReport(
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "status", required = false) String status,
            Model model) {

        model.addAttribute("reports", reportService.getEmployeeReport(departmentId, status));
        model.addAttribute("departments", configService.getAllDepartments());
        model.addAttribute("selectedDept", departmentId);
        model.addAttribute("selectedStatus", status);

        return "admin/reports/employees";
    }

    @GetMapping("/leaves")
    public String leaveUtilizationReport(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            Model model) {

        model.addAttribute("reports", reportService.getLeaveUtilizationReport(startDate, endDate, departmentId));
        model.addAttribute("departments", configService.getAllDepartments());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("selectedDept", departmentId);

        return "admin/reports/leaves";
    }
}
