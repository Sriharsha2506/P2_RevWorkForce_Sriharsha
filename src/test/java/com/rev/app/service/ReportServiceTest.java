package com.rev.app.service;

import com.rev.app.dto.DashboardMetricsDto;
import com.rev.app.entity.Department;
import com.rev.app.entity.Employee;
import com.rev.app.entity.User;
import com.rev.app.repository.AnnouncementRepository;
import com.rev.app.repository.DepartmentRepository;
import com.rev.app.repository.EmployeeRepository;
import com.rev.app.repository.LeaveApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LeaveApplicationRepository leaveApplicationRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private AnnouncementRepository announcementRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void testGetDashboardMetrics_ReturnsCorrectCounts() {
        User activeUser = new User();
        activeUser.setIsActive(1);

        Employee emp1 = new Employee();
        emp1.setUser(activeUser);
        Employee emp2 = new Employee();
        emp2.setUser(activeUser);

        when(employeeRepository.count()).thenReturn(2L);
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(emp1, emp2));
        when(departmentRepository.count()).thenReturn(3L);
        when(leaveApplicationRepository.findByStatus("APPROVED")).thenReturn(Collections.emptyList());
        when(leaveApplicationRepository.findByStatus("PENDING")).thenReturn(Collections.emptyList());
        when(announcementRepository.count()).thenReturn(5L);

        DashboardMetricsDto metrics = reportService.getDashboardMetrics();

        assertNotNull(metrics);
        assertEquals(2L, metrics.getTotalEmployees());
        assertEquals(2L, metrics.getActiveEmployees());
        assertEquals(3L, metrics.getTotalDepartments());
        assertEquals(5L, metrics.getOpenAnnouncements());
    }

    @Test
    void testGetEmployeeReport_NoDepartmentFilter_ReturnsAllEmployees() {
        User user = new User();
        user.setEmail("emp@rev.com");

        Department dept = new Department();
        dept.setDepartmentName("IT");

        Employee emp = new Employee();
        emp.setEmpId("E001");
        emp.setFirstName("Alice");
        emp.setLastName("Smith");
        emp.setUser(user);
        emp.setDepartment(dept);

        when(employeeRepository.findAll()).thenReturn(List.of(emp));

        var result = reportService.getEmployeeReport(null, null);

        assertEquals(1, result.size());
        assertEquals("E001", result.get(0).getEmpId());
        assertEquals("Alice", result.get(0).getFirstName());
    }
}
