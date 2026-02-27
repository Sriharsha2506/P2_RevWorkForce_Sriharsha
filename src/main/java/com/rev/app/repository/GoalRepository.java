package com.rev.app.repository;

import com.rev.app.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository
        extends JpaRepository<Goal, Long> {

    List<Goal> findByEmployeeEmployeeId(Long employeeId);
}