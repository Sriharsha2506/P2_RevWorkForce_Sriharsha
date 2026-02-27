package com.rev.app.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "LEAVE_TYPES")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveTypeId;

    private String name;
    private int quota;

    @OneToMany(mappedBy = "leaveType")
    private List<Leave> leaves;

    @OneToMany(mappedBy = "leaveType")
    private List<EmployeeLeaveBalance> balances;

    public Long getLeaveTypeId() { return leaveTypeId; }
    public void setLeaveTypeId(Long leaveTypeId) { this.leaveTypeId = leaveTypeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuota() { return quota; }
    public void setQuota(int quota) { this.quota = quota; }

    public List<Leave> getLeaves() { return leaves; }
    public void setLeaves(List<Leave> leaves) { this.leaves = leaves; }

    public List<EmployeeLeaveBalance> getBalances() { return balances; }
    public void setBalances(List<EmployeeLeaveBalance> balances) { this.balances = balances; }
}