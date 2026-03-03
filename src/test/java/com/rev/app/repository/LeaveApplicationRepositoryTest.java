package com.rev.app.repository;

import com.rev.app.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LeaveApplicationRepositoryTest {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    private Employee createEmployee(String empId, String email) {
        User user = new User();
        user.setEmail(email);
        user.setPassword("pwd");
        user.setRole(User.Role.ROLE_EMPLOYEE);
        user.setIsActive(1);
        userRepository.save(user);

        Employee emp = new Employee();
        emp.setEmpId(empId);
        emp.setFirstName("Test");
        emp.setLastName("User");
        emp.setUser(user);
        emp.setJoiningDate(LocalDate.now());
        return employeeRepository.save(emp);
    }

    @Test
    void testFindByStatus_ReturnsMatchingLeaves() {
        Employee emp = createEmployee("LEAVE001", "leave_user@rev.com");

        LeaveType leaveType = new LeaveType();
        leaveType.setLeaveName("Annual");
        leaveType.setMaxPerYear(15);
        leaveTypeRepository.save(leaveType);

        LeaveApplication leave = new LeaveApplication();
        leave.setEmployee(emp);
        leave.setLeaveType(leaveType);
        leave.setStartDate(LocalDate.now().plusDays(1));
        leave.setEndDate(LocalDate.now().plusDays(3));
        leave.setStatus("PENDING");
        leaveApplicationRepository.save(leave);

        List<LeaveApplication> pending = leaveApplicationRepository.findByStatus("PENDING");
        assertFalse(pending.isEmpty());
        assertEquals("PENDING", pending.get(0).getStatus());
    }

    @Test
    void testFindByEmployeeEmpId_ReturnsPaged() {
        Employee emp = createEmployee("LEAVE002", "leave_user2@rev.com");

        LeaveType leaveType = new LeaveType();
        leaveType.setLeaveName("Sick");
        leaveType.setMaxPerYear(10);
        leaveTypeRepository.save(leaveType);

        LeaveApplication leave = new LeaveApplication();
        leave.setEmployee(emp);
        leave.setLeaveType(leaveType);
        leave.setStartDate(LocalDate.now().plusDays(5));
        leave.setEndDate(LocalDate.now().plusDays(6));
        leave.setStatus("APPROVED");
        leaveApplicationRepository.save(leave);

        var page = leaveApplicationRepository.findByEmployee_EmpId("LEAVE002", PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
    }
}
