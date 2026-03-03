package com.rev.app.service;

import com.rev.app.dto.LeaveDto;
import com.rev.app.entity.*;
import com.rev.app.exceptions.BusinessException;
import com.rev.app.mapper.DTOMapper;
import com.rev.app.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveServiceTest {

    @Mock
    private LeaveApplicationRepository leaveApplicationRepository;

    @Mock
    private LeaveBalanceRepository leaveBalanceRepository;

    @Mock
    private LeaveTypeRepository leaveTypeRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private LeaveServiceImpl leaveService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(leaveService, "dtoMapper", new DTOMapper());
    }

    @Test
    void testApplyLeave_EndDateBeforeStartDate_ThrowsBusinessException() {
        LeaveDto dto = new LeaveDto();
        dto.setStartDate(LocalDate.now().plusDays(5));
        dto.setEndDate(LocalDate.now().plusDays(1)); // end before start

        assertThrows(BusinessException.class, () -> leaveService.applyLeave(dto));
        verifyNoInteractions(leaveApplicationRepository);
    }

    @Test
    void testApplyLeave_StartDateInPast_ThrowsBusinessException() {
        LeaveDto dto = new LeaveDto();
        dto.setStartDate(LocalDate.now().minusDays(1)); // past date
        dto.setEndDate(LocalDate.now().plusDays(2));

        assertThrows(BusinessException.class, () -> leaveService.applyLeave(dto));
        verifyNoInteractions(leaveApplicationRepository);
    }

    @Test
    void testApplyLeave_ValidRequest_SavesLeave() {
        LeaveDto dto = new LeaveDto();
        dto.setStartDate(LocalDate.now().plusDays(1));
        dto.setEndDate(LocalDate.now().plusDays(3));
        dto.setReason("Family event");
        dto.setEmpId("E001");
        dto.setLeaveTypeId(1L);

        Employee emp = new Employee();
        emp.setEmpId("E001");
        emp.setFirstName("John");
        emp.setLastName("Doe");
        emp.setManager(null); // no manager

        LeaveType leaveType = new LeaveType();
        leaveType.setLeaveTypeId(1L);
        leaveType.setLeaveName("Annual");

        LeaveApplication savedLeave = new LeaveApplication();
        savedLeave.setEmployee(emp);
        savedLeave.setLeaveType(leaveType);
        savedLeave.setStartDate(dto.getStartDate());
        savedLeave.setEndDate(dto.getEndDate());
        savedLeave.setStatus("PENDING");

        when(employeeRepository.findByIdWithManagerAndUser("E001")).thenReturn(Optional.of(emp));
        when(leaveTypeRepository.findById(1L)).thenReturn(Optional.of(leaveType));
        when(leaveApplicationRepository.save(any())).thenReturn(savedLeave);

        LeaveDto result = leaveService.applyLeave(dto);

        assertNotNull(result);
        verify(leaveApplicationRepository, times(1)).save(any(LeaveApplication.class));
    }

    @Test
    void testCancelLeave_PendingLeave_Succeeds() {
        LeaveApplication leave = new LeaveApplication();
        leave.setLeaveId(1L);
        leave.setStatus("PENDING");

        Employee emp = new Employee();
        emp.setEmpId("E001");
        LeaveType leaveType = new LeaveType();
        leave.setEmployee(emp);
        leave.setLeaveType(leaveType);

        when(leaveApplicationRepository.findById(1L)).thenReturn(Optional.of(leave));
        when(leaveApplicationRepository.save(any())).thenReturn(leave);

        LeaveDto result = leaveService.cancelLeave(1L);

        assertNotNull(result);
        verify(leaveApplicationRepository).save(argThat(l -> "CANCELLED".equals(l.getStatus())));
    }

    @Test
    void testCancelLeave_ApprovedLeave_ThrowsBusinessException() {
        LeaveApplication leave = new LeaveApplication();
        leave.setLeaveId(2L);
        leave.setStatus("APPROVED");

        when(leaveApplicationRepository.findById(2L)).thenReturn(Optional.of(leave));

        assertThrows(BusinessException.class, () -> leaveService.cancelLeave(2L));
        verify(leaveApplicationRepository, never()).save(any());
    }
}
