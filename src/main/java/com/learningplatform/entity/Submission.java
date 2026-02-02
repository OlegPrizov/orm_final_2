package com.learningplatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "submissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_submission_student_assignment",
                        columnNames = {"student_id", "assignment_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {
        "assignment",
        "student"
})
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Задание, к которому относится решение
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    /**
     * Студент, отправивший решение
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    /**
     * Дата и время отправки
     */
    @Column(nullable = false)
    private LocalDateTime submittedAt;

    /**
     * Содержимое решения (текст или путь к файлу)
     */
    @Column(length = 4000)
    private String content;

    /**
     * Оценка (null — если ещё не проверено)
     */
    private Integer score;

    /**
     * Комментарий преподавателя
     */
    @Column(length = 2000)
    private String feedback;
}

