package com.learningplatform.entity;

import jakarta.persistence.*;
        import lombok.*;

        import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {
        "module",
        "questions",
        "quizSubmissions"
})
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;

    /**
     * Модуль, к которому относится тест
     * (один модуль — один тест)
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false, unique = true)
    private Module module;

    /**
     * Вопросы теста
     */
    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

    /**
     * Результаты прохождения теста
     */
    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<QuizSubmission> quizSubmissions = new ArrayList<>();
}

