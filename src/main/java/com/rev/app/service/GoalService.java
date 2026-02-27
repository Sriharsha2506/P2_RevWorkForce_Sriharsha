package com.rev.app.service;

import com.rev.app.entity.Goal;
import java.util.List;

public interface GoalService {

    Goal saveGoal(Goal goal);

    List<Goal> getGoalsByEmployee(Long employeeId);
}