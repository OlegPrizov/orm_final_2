package com.learningplatform.service;

import com.learningplatform.entity.Assignment;
import com.learningplatform.entity.Submission;
import com.learningplatform.entity.User;
import com.learningplatform.repository.AssignmentRepository;
import com.learningplatform.repository.SubmissionRepository;
import com.learningplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Submission submit(Long assignmentId, Long studentId, String content) {
        if (submissionRepository.findByStudentIdAndAssignmentId(studentId, assignmentId).isPresent()) {
            throw new IllegalStateException("Submission already exists for this student and assignment");
        }

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found: " + assignmentId));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + studentId));

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setContent(content);

        return submissionRepository.save(submission);
    }
}

