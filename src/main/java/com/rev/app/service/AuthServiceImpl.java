package com.rev.app.service;

import com.rev.app.entity.Employee;
import com.rev.app.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository repository;

    // Constructor Injection
    public AuthServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee login(String email, String password) {

        Employee employee = repository.findByEmail(email);

        if (employee != null && employee.getPassword().equals(password)) {
            return employee;
        }

        return null;
    }
}