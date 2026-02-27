package com.rev.app.service;

import com.rev.app.entity.Employee;
import com.rev.app.entity.Leave;
import com.rev.app.repository.LeaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;

    public LeaveServiceImpl(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    @Override
    public Leave applyLeave(Leave leave) {
        leave.setStatus("PENDING");
        return leaveRepository.save(leave);
    }

    @Override
    public List<Leave> getEmployeeLeaves(Employee employee) {
        return leaveRepository.findByEmployee(employee);
    }

    @Override
    public List<Leave> getPendingLeaves() {
        return leaveRepository.findByStatus("PENDING");
    }

    @Override
    public Leave approveLeave(Long id) {
        Leave leave = leaveRepository.findById(id).orElse(null);
        if (leave != null) {
            leave.setStatus("APPROVED");
            return leaveRepository.save(leave);
        }
        return null;
    }

    @Override
    public Leave rejectLeave(Long id) {
        Leave leave = leaveRepository.findById(id).orElse(null);
        if (leave != null) {
            leave.setStatus("REJECTED");
            return leaveRepository.save(leave);
        }
        return null;
    }
}