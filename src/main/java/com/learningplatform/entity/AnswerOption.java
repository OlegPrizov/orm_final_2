package com.learningplatform.entity;

import jakarta.persistence.*;
        import lombok.*;

@Entity
@Table(name = "answer_options")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "question")
public class AnswerOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Вопрос, к которому относится вариант ответа
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    /**
     * Текст варианта ответа
     */
    @Column(nullable = false, length = 1000)
    private String text;

    /**
     * Признак правильного ответа
     */
    @Column(nullable = false)
    private boolean isCorrect;
}
