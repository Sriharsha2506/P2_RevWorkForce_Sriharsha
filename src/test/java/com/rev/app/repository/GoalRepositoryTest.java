package com.rev.app.repository;

import com.rev.app.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GoalRepositoryTest {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    private Employee createEmployee(String empId, String email) {
        User user = new User();
        user.setEmail(email);
        user.setPassword("pwd");
        user.setRole(User.Role.ROLE_EMPLOYEE);
        user.setIsActive(1);
        userRepository.save(user);

        Employee emp = new Employee();
        emp.setEmpId(empId);
        emp.setFirstName("Goal");
        emp.setLastName("Tester");
        emp.setUser(user);
        emp.setJoiningDate(LocalDate.now());
        return employeeRepository.save(emp);
    }

    @Test
    void testSaveAndFindByEmployeeEmpId_ReturnsGoals() {
        Employee emp = createEmployee("GOAL001", "goal_user@rev.com");

        Goal goal = new Goal();
        goal.setEmployee(emp);
        goal.setGoalDesc("Increase productivity by 20%");
        goal.setStatus("NOT_STARTED");
        goal.setProgress(0);
        goalRepository.save(goal);

        Page<Goal> page = goalRepository.findByEmployee_EmpId("GOAL001", PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("Increase productivity by 20%", page.getContent().get(0).getGoalDesc());
    }

    @Test
    void testDeleteGoal_RemovesRecord() {
        Employee emp = createEmployee("GOAL002", "goal_user2@rev.com");

        Goal goal = new Goal();
        goal.setEmployee(emp);
        goal.setGoalDesc("Temp goal");
        goal.setStatus("NOT_STARTED");
        goal.setProgress(0);
        Goal saved = goalRepository.save(goal);

        goalRepository.deleteById(saved.getGoalId());
        assertFalse(goalRepository.findById(saved.getGoalId()).isPresent());
    }
}
