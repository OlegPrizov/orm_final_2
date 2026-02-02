package com.learningplatform.service;

import com.learningplatform.entity.Category;
import com.learningplatform.entity.Course;
import com.learningplatform.entity.Enrollment;
import com.learningplatform.entity.User;
import com.learningplatform.entity.EnrollmentStatus;
import com.learningplatform.entity.UserRole;
import com.learningplatform.repository.CategoryRepository;
import com.learningplatform.repository.CourseRepository;
import com.learningplatform.repository.EnrollmentRepository;
import com.learningplatform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class EnrollmentServiceIT {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void enrollStudent_success() {
        // given
        User student = new User();
        student.setName("John Student");
        student.setEmail("student@test.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        Category category = new Category();
        category.setName("Programming");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Java Basics");
        course.setCategory(category);
        course = courseRepository.save(course);

        // when
        Enrollment enrollment = enrollmentService.enroll(student.getId(), course.getId());

        // then
        assertThat(enrollment.getId()).isNotNull();
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.ACTIVE);
        assertThat(enrollment.getEnrollDate()).isNotNull();

        Optional<Enrollment> fromDb =
                enrollmentRepository.findByUserIdAndCourseId(student.getId(), course.getId());

        assertThat(fromDb).isPresent();
    }

    @Test
    void enrollStudent_twice_shouldFail() {
        // given
        User student = new User();
        student.setName("Jane Student");
        student.setEmail("student2@test.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        Category category = new Category();
        category.setName("Databases");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("PostgreSQL");
        course.setCategory(category);
        course = courseRepository.save(course);

        enrollmentService.enroll(student.getId(), course.getId());

        // when / then
        User finalStudent = student;
        Course finalCourse = course;
        assertThrows(
                IllegalStateException.class,
                () -> enrollmentService.enroll(finalStudent.getId(), finalCourse.getId())
        );
    }
}