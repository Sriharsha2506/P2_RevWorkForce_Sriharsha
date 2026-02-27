package com.rev.app.service;

import com.rev.app.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee login(String email, String password);

    List<Employee> getAllEmployees();

    Employee saveEmployee(Employee employee);

    void deleteEmployee(Long id);

    Optional<Employee> fetchEmployeeById(Long id);

    Employee getEmployeeById(Long id);   // ✅ Added
}