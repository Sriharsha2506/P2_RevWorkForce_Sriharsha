package com.rev.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "EMPLOYEES")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMP_ID")
    private Long employeeId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "STATUS")
    private String status;   // ACTIVE / INACTIVE

    @Column(name = "ROLE")
    private String role;     // ADMIN / MANAGER / EMPLOYEE


    // ===============================
    // DEPARTMENT RELATIONSHIP
    // ===============================
    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;


    // ===============================
    // REPORTING MANAGER RELATIONSHIP
    // ===============================
    @ManyToOne
    @JoinColumn(name = "MANAGER_ID")
    private Employee reportingManager;


    public Employee() {}


    // ===============================
    // GETTERS & SETTERS
    // ===============================

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee getReportingManager() {
        return reportingManager;
    }

    public void setReportingManager(Employee reportingManager) {
        this.reportingManager = reportingManager;
    }
}