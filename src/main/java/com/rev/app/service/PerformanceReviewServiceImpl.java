package com.rev.app.service;

import com.rev.app.entity.PerformanceReview;
import com.rev.app.repository.PerformanceReviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

    @Autowired
    private PerformanceReviewRepository reviewRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PerformanceReview submitReview(PerformanceReview review) {
        review.setStatus("SUBMITTED");
        entityManager.persist(review);
        return review;
    }

    @Override
    public List<PerformanceReview> getReviewsByEmployee(Long employeeId) {
        return reviewRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<PerformanceReview> getAllReviews() {
        return entityManager
                .createQuery("SELECT r FROM PerformanceReview r", PerformanceReview.class)
                .getResultList();
    }

    @Override
    public PerformanceReview updateReview(Long reviewId, PerformanceReview updatedReview) {

        PerformanceReview existing = entityManager.find(PerformanceReview.class, reviewId);

        if (existing == null) {
            throw new RuntimeException("Review not found with ID: " + reviewId);
        }

        existing.setComments(updatedReview.getComments());
        existing.setRating(updatedReview.getRating());
        existing.setReviewPeriod(updatedReview.getReviewPeriod());
        existing.setStatus(updatedReview.getStatus());

        entityManager.merge(existing);
        return existing;
    }

    @Override
    public void deleteReview(Long reviewId) {

        PerformanceReview review = entityManager.find(PerformanceReview.class, reviewId);

        if (review == null) {
            throw new RuntimeException("Review not found with ID: " + reviewId);
        }

        reviewRepository.deleteReviewById(reviewId);
    }
}