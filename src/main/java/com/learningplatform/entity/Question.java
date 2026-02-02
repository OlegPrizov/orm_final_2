package com.learningplatform.entity;

import com.learningplatform.entity.QuestionType;
import jakarta.persistence.*;
        import lombok.*;

        import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {
        "quiz",
        "options"
})
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Тест, к которому относится вопрос
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    /**
     * Текст вопроса
     */
    @Column(nullable = false, length = 2000)
    private String text;

    /**
     * Тип вопроса
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    /**
     * Варианты ответов
     */
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<AnswerOption> options = new ArrayList<>();
}
