package com.rev.app.repository;

import com.rev.app.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {

    LeaveType findByName(String name);

    @Query("SELECT lt FROM LeaveType lt WHERE lt.quota > :days")
    List<LeaveType> findByQuotaGreaterThan(@Param("days") int days);
}