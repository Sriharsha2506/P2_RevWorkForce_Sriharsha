package com.rev.app.repository;

import com.rev.app.entity.PerformanceReview;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {

    @Query("SELECT r FROM PerformanceReview r WHERE r.employee.employeeId = :empId")
    List<PerformanceReview> findByEmployeeId(@Param("empId") Long empId);

    @Query("SELECT r FROM PerformanceReview r WHERE r.manager.employeeId = :managerId")
    List<PerformanceReview> findByManagerId(@Param("managerId") Long managerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PerformanceReview r WHERE r.reviewId = :id")
    void deleteReviewById(@Param("id") Long id);
}