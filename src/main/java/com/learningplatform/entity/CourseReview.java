package com.learningplatform.entity;

import jakarta.persistence.*;
        import lombok.*;

        import java.time.LocalDateTime;

@Entity
@Table(name = "course_reviews")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {
        "course",
        "student"
})
public class CourseReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Курс, к которому относится отзыв
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * Студент, оставивший отзыв
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    /**
     * Оценка курса (например, 1–5)
     */
    @Column(nullable = false)
    private Integer rating;

    /**
     * Текст отзыва
     */
    @Column(length = 2000)
    private String comment;

    /**
     * Дата создания отзыва
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
