package com.learningplatform.repository;

import com.learningplatform.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
    List<CourseReview> findByCourseId(Long courseId);
    List<CourseReview> findByStudentId(Long studentId);
}

