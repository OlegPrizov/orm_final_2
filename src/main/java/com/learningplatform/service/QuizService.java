package com.learningplatform.service;

import com.learningplatform.entity.Quiz;
import com.learningplatform.entity.QuizSubmission;
import com.learningplatform.entity.User;
import com.learningplatform.repository.QuizRepository;
import com.learningplatform.repository.QuizSubmissionRepository;
import com.learningplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final UserRepository userRepository;

    @Transactional
    public QuizSubmission submitQuiz(Long quizId, Long studentId, Integer score) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found: " + quizId));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + studentId));

        QuizSubmission qs = new QuizSubmission();
        qs.setQuiz(quiz);
        qs.setStudent(student);
        qs.setScore(score);
        qs.setTakenAt(LocalDateTime.now());

        return quizSubmissionRepository.save(qs);
    }
}
