package com.learningplatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {
        "lesson",
        "submissions"
})
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 4000)
    private String description;

    /**
     * Дедлайн сдачи задания
     */
    private LocalDateTime dueDate;

    /**
     * Максимальный балл
     */
    private Integer maxScore;

    /**
     * Урок, к которому относится задание
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    /**
     * Решения студентов
     */
    @OneToMany(mappedBy = "assignment", fetch = FetchType.LAZY)
    private List<Submission> submissions = new ArrayList<>();
}
