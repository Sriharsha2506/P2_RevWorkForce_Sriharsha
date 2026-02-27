package com.rev.app.service;

import com.rev.app.entity.Employee;
import com.rev.app.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    // ==========================
    // LOGIN
    // ==========================
    @Override
    public Employee login(String email, String password) {

        Employee employee = repository.findByEmail(email);

        if (employee != null && employee.getPassword().equals(password)) {
            return employee;
        }

        return null;
    }

    // ==========================
    // GET ALL EMPLOYEES
    // ==========================
    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // ==========================
    // SAVE EMPLOYEE
    // ==========================
    @Override
    public Employee saveEmployee(Employee employee) {
        return repository.save(employee);
    }

    // ==========================
    // DELETE EMPLOYEE
    // ==========================
    @Override
    public void deleteEmployee(Long id) {
        repository.deleteById(id);
    }

    // ==========================
    // FETCH EMPLOYEE BY ID (Optional)
    // ==========================
    @Override
    public Optional<Employee> fetchEmployeeById(Long id) {
        return repository.findById(id);
    }

    // ==========================
    // GET EMPLOYEE BY ID (Direct)
    // Used for Performance Review
    // ==========================
    @Override
    public Employee getEmployeeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
    }
}