package com.rev.app.repository;

import com.rev.app.entity.Employee;
import com.rev.app.entity.LeaveType;
import com.rev.app.entity.EmployeeLeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeLeaveBalanceRepository
        extends JpaRepository<EmployeeLeaveBalance, Long> {

    EmployeeLeaveBalance findByEmployeeAndLeaveType(
            Employee employee,
            LeaveType leaveType
    );
}