package com.rev.app.service;

import com.rev.app.dto.PerformanceReviewDto;
import com.rev.app.entity.*;
import com.rev.app.exceptions.ResourceNotFoundException;
import com.rev.app.mapper.DTOMapper;
import com.rev.app.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceTest {

    @Mock
    private PerformanceReviewRepository performanceReviewRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PerformanceServiceImpl performanceService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(performanceService, "dtoMapper", new DTOMapper());
    }

    @Test
    void testSubmitReview_ValidEmployee_SavesReview() {
        PerformanceReviewDto dto = new PerformanceReviewDto();
        dto.setEmpId("E001");
        dto.setReviewYear(2025);
        dto.setAchievements("Completed all targets");
        dto.setImprovements("Better time management");
        dto.setKeyDeliverables("Delivered Q3 project");
        dto.setSelfRating(new BigDecimal("4.0"));

        User user = new User();
        user.setUserId(1L);
        Employee emp = new Employee();
        emp.setEmpId("E001");
        emp.setFirstName("John");
        emp.setLastName("Doe");
        emp.setUser(user);
        emp.setManager(null); // no manager, no notification

        PerformanceReview saved = new PerformanceReview();
        saved.setEmployee(emp);
        saved.setReviewYear(2025);
        saved.setStatus("SUBMITTED");

        when(employeeRepository.findById("E001")).thenReturn(Optional.of(emp));
        when(performanceReviewRepository.save(any())).thenReturn(saved);

        PerformanceReviewDto result = performanceService.submitReview(dto);

        assertNotNull(result);
        verify(performanceReviewRepository, times(1)).save(any(PerformanceReview.class));
        verifyNoInteractions(notificationService); // no manager, no notification
    }

    @Test
    void testSubmitReview_EmployeeNotFound_ThrowsException() {
        PerformanceReviewDto dto = new PerformanceReviewDto();
        dto.setEmpId("INVALID");

        when(employeeRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> performanceService.submitReview(dto));
        verify(performanceReviewRepository, never()).save(any());
    }

    @Test
    void testProvideFeedback_ValidReview_UpdatesStatus() {
        User user = new User();
        user.setUserId(10L);
        Employee emp = new Employee();
        emp.setUser(user);

        PerformanceReview review = new PerformanceReview();
        review.setEmployee(emp);
        review.setStatus("SUBMITTED");
        review.setReviewId(1L);

        PerformanceReview saved = new PerformanceReview();
        saved.setEmployee(emp);
        saved.setStatus("REVIEWED");
        saved.setManagerRating(new BigDecimal("4.5"));

        when(performanceReviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(performanceReviewRepository.save(any())).thenReturn(saved);

        PerformanceReviewDto result = performanceService.provideFeedback(1L, new BigDecimal("4.5"), "Great work!");

        assertNotNull(result);
        assertEquals("REVIEWED", result.getStatus());
        verify(notificationService, times(1)).sendNotification(eq(10L), anyString(), anyString());
    }

    @Test
    void testGetReviewById_NotFound_ThrowsException() {
        when(performanceReviewRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> performanceService.getReviewById(999L));
    }
}
