package com.learningplatform.service;

import com.learningplatform.entity.*;
import com.learningplatform.entity.QuestionType;
import com.learningplatform.entity.UserRole;
import com.learningplatform.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuizServiceIT {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Test
    void submitQuiz_success() {
        // given: student
        User student = new User();
        student.setName("Student One");
        student.setEmail("quiz_student@test.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        // given: course -> module -> quiz
        Category category = new Category();
        category.setName("Testing");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Java Tests");
        course.setCategory(category);
        course = courseRepository.save(course);

        com.learningplatform.entity.Module module = new com.learningplatform.entity.Module();
        module.setTitle("Module 1");
        module.setOrderIndex(1);
        module.setCourse(course);
        module = moduleRepository.save(module);

        Quiz quiz = new Quiz();
        quiz.setTitle("Quiz 1");
        quiz.setModule(module);
        quiz = quizRepository.save(quiz);

        // (не обязательно для QuizSubmission, но полезно показать, что структура теста есть)
        Question q = new Question();
        q.setQuiz(quiz);
        q.setText("2 + 2 = ?");
        q.setType(QuestionType.SINGLE_CHOICE);
        q = questionRepository.save(q);

        AnswerOption o1 = new AnswerOption();
        o1.setQuestion(q);
        o1.setText("4");
        o1.setCorrect(true);
        answerOptionRepository.save(o1);

        // when: submit quiz result
        var saved = quizService.submitQuiz(quiz.getId(), student.getId(), 100);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getScore()).isEqualTo(100);
        assertThat(saved.getTakenAt()).isNotNull();

        // and: persisted
        assertThat(quizSubmissionRepository.findById(saved.getId())).isPresent();

        // optional: check reverse collections work inside transaction
        Quiz fromDbQuiz = quizRepository.findById(quiz.getId()).orElseThrow();
        assertThat(fromDbQuiz.getQuizSubmissions()).hasSize(1);

        User fromDbStudent = userRepository.findById(student.getId()).orElseThrow();
        assertThat(fromDbStudent.getQuizSubmissions()).hasSize(1);
    }
}