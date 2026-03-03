package com.rev.app.repository;

import com.rev.app.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LeaveBalanceRepositoryTest {

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

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
        emp.setFirstName("Balance");
        emp.setLastName("Tester");
        emp.setUser(user);
        emp.setJoiningDate(LocalDate.now());
        return employeeRepository.save(emp);
    }

    @Test
    void testFindByEmployeeEmpIdAndLeaveTypeId_ReturnsBalance() {
        Employee emp = createEmployee("BAL001", "balance_user@rev.com");

        LeaveType lt = new LeaveType();
        lt.setLeaveName("Casual");
        lt.setMaxPerYear(12);
        leaveTypeRepository.save(lt);

        LeaveBalance balance = new LeaveBalance();
        balance.setEmployee(emp);
        balance.setLeaveType(lt);
        balance.setBalanceDays(10);
        leaveBalanceRepository.save(balance);

        Optional<LeaveBalance> found = leaveBalanceRepository
                .findByEmployee_EmpIdAndLeaveType_LeaveTypeId("BAL001", lt.getLeaveTypeId());

        assertTrue(found.isPresent());
        assertEquals(10, found.get().getBalanceDays());
    }

    @Test
    void testFindByEmployeeEmpId_ReturnsList() {
        Employee emp = createEmployee("BAL002", "balance_user2@rev.com");

        LeaveType lt1 = new LeaveType();
        lt1.setLeaveName("Annual");
        lt1.setMaxPerYear(15);
        leaveTypeRepository.save(lt1);

        LeaveBalance b1 = new LeaveBalance();
        b1.setEmployee(emp);
        b1.setLeaveType(lt1);
        b1.setBalanceDays(20);
        leaveBalanceRepository.save(b1);

        List<LeaveBalance> balances = leaveBalanceRepository.findByEmployee_EmpId("BAL002");
        assertEquals(1, balances.size());
    }
}
