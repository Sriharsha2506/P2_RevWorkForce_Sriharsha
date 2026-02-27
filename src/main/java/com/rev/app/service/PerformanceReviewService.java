package com.rev.app.service;

import com.rev.app.entity.PerformanceReview;
import java.util.List;

public interface PerformanceReviewService {

    PerformanceReview submitReview(PerformanceReview review);

    List<PerformanceReview> getReviewsByEmployee(Long employeeId);

    List<PerformanceReview> getAllReviews();

    PerformanceReview updateReview(Long reviewId, PerformanceReview review);

    void deleteReview(Long reviewId);
}