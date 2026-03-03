package com.rev.app.repository;

import com.rev.app.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PerformanceReviewRepositoryTest {

    @Autowired
    private PerformanceReviewRepository performanceReviewRepository;

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
        emp.setFirstName("Perf");
        emp.setLastName("Tester");
        emp.setUser(user);
        emp.setJoiningDate(LocalDate.now());
        return employeeRepository.save(emp);
    }

    @Test
    void testSaveAndFindByEmployeeEmpId_ReturnsReview() {
        Employee emp = createEmployee("PERF001", "perf_user@rev.com");

        PerformanceReview review = new PerformanceReview();
        review.setEmployee(emp);
        review.setReviewYear(2025);
        review.setAchievements("Met all KPIs");
        review.setImprovements("Better documentation");
        review.setKeyDeliverables("Project Alpha");
        review.setSelfRating(new BigDecimal("4.0"));
        review.setStatus("SUBMITTED");
        performanceReviewRepository.save(review);

        Page<PerformanceReview> page = performanceReviewRepository.findByEmployee_EmpId("PERF001",
                PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("SUBMITTED", page.getContent().get(0).getStatus());
    }

    @Test
    void testFindById_ReturnsReview() {
        Employee emp = createEmployee("PERF002", "perf_user2@rev.com");

        PerformanceReview review = new PerformanceReview();
        review.setEmployee(emp);
        review.setReviewYear(2024);
        review.setStatus("REVIEWED");
        PerformanceReview saved = performanceReviewRepository.save(review);

        assertTrue(performanceReviewRepository.findById(saved.getReviewId()).isPresent());
    }
}
