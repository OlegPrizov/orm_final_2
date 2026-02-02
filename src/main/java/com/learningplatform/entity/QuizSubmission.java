package com.learningplatform.entity;

import jakarta.persistence.*;
        import lombok.*;

        import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_submissions")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {
        "quiz",
        "student"
})
public class QuizSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Тест, который был пройден
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    /**
     * Студент, прошедший тест
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    /**
     * Итоговый результат (баллы или процент)
     */
    @Column(nullable = false)
    private Integer score;

    /**
     * Дата и время прохождения теста
     */
    @Column(nullable = false)
    private LocalDateTime takenAt;
}
