package com.rev.app.service;

import com.rev.app.entity.Goal;
import com.rev.app.repository.GoalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalServiceImpl implements GoalService {

    private final GoalRepository repository;

    public GoalServiceImpl(GoalRepository repository) {
        this.repository = repository;
    }

    @Override
    public Goal saveGoal(Goal goal) {
        return repository.save(goal);
    }

    @Override
    public List<Goal> getGoalsByEmployee(Long employeeId) {
        return repository.findByEmployeeEmployeeId(employeeId);
    }
}