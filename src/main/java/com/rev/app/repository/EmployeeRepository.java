package com.rev.app.repository;

import com.rev.app.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmail(String email);
    List<Employee> findByRole(String role);
    long countByRole(String role);
}