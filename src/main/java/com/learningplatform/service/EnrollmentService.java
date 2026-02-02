package com.learningplatform.service;

import com.learningplatform.entity.Course;
import com.learningplatform.entity.Enrollment;
import com.learningplatform.entity.User;
import com.learningplatform.entity.EnrollmentStatus;
import com.learningplatform.repository.CourseRepository;
import com.learningplatform.repository.EnrollmentRepository;
import com.learningplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Enrollment enroll(Long userId, Long courseId) {
        if (enrollmentRepository.findByUserIdAndCourseId(userId, courseId).isPresent()) {
            throw new IllegalStateException("User is already enrolled to this course");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        return enrollmentRepository.save(enrollment);
    }
}

