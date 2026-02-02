package com.learningplatform.repository;

import com.learningplatform.entity.Category;
import com.learningplatform.entity.Course;
import com.learningplatform.entity.Enrollment;
import com.learningplatform.entity.User;
import com.learningplatform.entity.EnrollmentStatus;
import com.learningplatform.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class EnrollmentRepositoryIT {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByUserIdAndCourseId_success() {
        User student = new User();
        student.setName("Student");
        student.setEmail("enr_student@test.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        Category cat = new Category();
        cat.setName("DB");
        cat = categoryRepository.save(cat);

        Course course = new Course();
        course.setTitle("Postgres");
        course.setCategory(cat);
        course = courseRepository.save(course);

        Enrollment e = new Enrollment();
        e.setUser(student);
        e.setCourse(course);
        e.setEnrollDate(LocalDateTime.now());
        e.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepository.save(e);

        assertThat(enrollmentRepository.findByUserIdAndCourseId(student.getId(), course.getId()))
                .isPresent();
        assertThat(enrollmentRepository.findByUserId(student.getId())).hasSize(1);
        assertThat(enrollmentRepository.findByCourseId(course.getId())).hasSize(1);
    }

    @Test
    void save_duplicateEnrollment_shouldFail() {
        User student = new User();
        student.setName("Student2");
        student.setEmail("enr_student2@test.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        Category cat = new Category();
        cat.setName("Algo");
        cat = categoryRepository.save(cat);

        Course course = new Course();
        course.setTitle("Algorithms");
        course.setCategory(cat);
        course = courseRepository.save(course);

        Enrollment e1 = new Enrollment();
        e1.setUser(student);
        e1.setCourse(course);
        e1.setEnrollDate(LocalDateTime.now());
        e1.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepository.save(e1);

        Enrollment e2 = new Enrollment();
        e2.setUser(student);
        e2.setCourse(course);
        e2.setEnrollDate(LocalDateTime.now());
        e2.setStatus(EnrollmentStatus.ACTIVE);

        assertThrows(DataIntegrityViolationException.class, () -> {
            enrollmentRepository.saveAndFlush(e2); // flush, чтобы словить uk(user_id, course_id)
        });
    }
}