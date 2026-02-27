package com.rev.app.service;

import com.rev.app.entity.Employee;
import com.rev.app.entity.Leave;

import java.util.List;

public interface LeaveService {

    Leave applyLeave(Leave leave);

    List<Leave> getEmployeeLeaves(Employee employee);

    List<Leave> getPendingLeaves();

    Leave approveLeave(Long id);

    Leave rejectLeave(Long id);
}