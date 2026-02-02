package com.learningplatform.service;

import com.learningplatform.entity.*;
        import com.learningplatform.entity.UserRole;
import com.learningplatform.repository.*;
        import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class SubmissionServiceIT {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Test
    void submit_success() {
        // given: student
        User student = new User();
        student.setName("Student One");
        student.setEmail("sub_student@test.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        // given: course -> module -> lesson -> assignment
        Category category = new Category();
        category.setName("Homework");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Java Core");
        course.setCategory(category);
        course = courseRepository.save(course);

        // IMPORTANT: avoid ambiguity with java.lang.Module
        com.learningplatform.entity.Module module = new com.learningplatform.entity.Module();
        module.setTitle("Module 1");
        module.setOrderIndex(1);
        module.setCourse(course);
        module = moduleRepository.save(module);

        Lesson lesson = new Lesson();
        lesson.setTitle("Lesson 1");
        lesson.setContent("Basics");
        lesson.setModule(module);
        lesson = lessonRepository.save(lesson);

        Assignment assignment = new Assignment();
        assignment.setTitle("HW 1");
        assignment.setDescription("Do it");
        assignment.setLesson(lesson);
        assignment = assignmentRepository.save(assignment);

        // when
        Submission saved = submissionService.submit(assignment.getId(), student.getId(), "my answer");

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getSubmittedAt()).isNotNull();
        assertThat(saved.getContent()).isEqualTo("my answer");
        assertThat(saved.getScore()).isNull(); // not graded yet

        assertThat(submissionRepository.findById(saved.getId())).isPresent();
        assertThat(submissionRepository.findByStudentIdAndAssignmentId(student.getId(), assignment.getId()))
                .isPresent();
    }

    @Test
    void submit_twice_shouldFail() {
        // given: student
        User student = new User();
        student.setName("Student Two");
        student.setEmail("sub_student2@test.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        // given: course -> module -> lesson -> assignment
        Category category = new Category();
        category.setName("Homework2");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Spring Intro");
        course.setCategory(category);
        course = courseRepository.save(course);

        com.learningplatform.entity.Module module = new com.learningplatform.entity.Module();
        module.setTitle("Module 1");
        module.setOrderIndex(1);
        module.setCourse(course);
        module = moduleRepository.save(module);

        Lesson lesson = new Lesson();
        lesson.setTitle("Lesson 1");
        lesson.setContent("Intro");
        lesson.setModule(module);
        lesson = lessonRepository.save(lesson);

        Assignment assignment = new Assignment();
        assignment.setTitle("HW 1");
        assignment.setDescription("First homework");
        assignment.setLesson(lesson);
        assignment = assignmentRepository.save(assignment);

        submissionService.submit(assignment.getId(), student.getId(), "first answer");

        // when / then
        Assignment finalAssignment = assignment;
        User finalStudent = student;
        assertThrows(
                IllegalStateException.class,
                () -> submissionService.submit(finalAssignment.getId(), finalStudent.getId(), "second answer")
        );
    }
}
