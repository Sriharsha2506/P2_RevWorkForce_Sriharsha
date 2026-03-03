package com.rev.app.service;

import com.rev.app.dto.GoalDto;
import com.rev.app.entity.Employee;
import com.rev.app.entity.Goal;
import com.rev.app.exceptions.ResourceNotFoundException;
import com.rev.app.mapper.DTOMapper;
import com.rev.app.repository.EmployeeRepository;
import com.rev.app.repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private GoalServiceImpl goalService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(goalService, "dtoMapper", new DTOMapper());
    }

    @Test
    void testCreateGoal_ValidEmployee_SavesGoal() {
        GoalDto dto = new GoalDto();
        dto.setEmpId("E001");
        dto.setGoalDesc("Improve code coverage to 80%");
        dto.setPriority("HIGH");

        Employee emp = new Employee();
        emp.setEmpId("E001");

        Goal savedGoal = new Goal();
        savedGoal.setEmployee(emp);
        savedGoal.setGoalDesc(dto.getGoalDesc());
        savedGoal.setStatus("NOT_STARTED");

        when(employeeRepository.findById("E001")).thenReturn(Optional.of(emp));
        when(goalRepository.save(any(Goal.class))).thenReturn(savedGoal);

        GoalDto result = goalService.createGoal(dto);

        assertNotNull(result);
        verify(goalRepository, times(1)).save(any(Goal.class));
    }

    @Test
    void testCreateGoal_EmployeeNotFound_ThrowsException() {
        GoalDto dto = new GoalDto();
        dto.setEmpId("INVALID");

        when(employeeRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> goalService.createGoal(dto));
        verify(goalRepository, never()).save(any());
    }

    @Test
    void testUpdateGoalStatus_ValidGoal_UpdatesStatus() {
        Employee emp = new Employee();
        Goal goal = new Goal();
        goal.setEmployee(emp);
        goal.setStatus("NOT_STARTED");

        Goal updated = new Goal();
        updated.setEmployee(emp);
        updated.setStatus("IN_PROGRESS");
        updated.setProgress(50);

        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(goalRepository.save(any())).thenReturn(updated);

        GoalDto result = goalService.updateGoalStatus(1L, "IN_PROGRESS", 50);

        assertNotNull(result);
        assertEquals("IN_PROGRESS", result.getStatus());
    }

    @Test
    void testUpdateGoalStatus_GoalNotFound_ThrowsException() {
        when(goalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> goalService.updateGoalStatus(99L, "DONE", 100));
    }
}
