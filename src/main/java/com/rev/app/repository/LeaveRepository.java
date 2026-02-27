package com.rev.app.repository;

import com.rev.app.entity.Leave;
import com.rev.app.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    List<Leave> findByEmployee(Employee employee);

    List<Leave> findByStatus(String status);

    long countByStatus(String status);
}