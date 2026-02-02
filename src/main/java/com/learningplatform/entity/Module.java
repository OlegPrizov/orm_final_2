package com.learningplatform.entity;

import jakarta.persistence.*;
        import lombok.*;

        import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modules")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {
        "course",
        "lessons"
})
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;

    /**
     * Порядок модуля внутри курса
     */
    @Column(nullable = false)
    private Integer orderIndex;

    /**
     * Курс, к которому относится модуль
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    /**
     * Уроки внутри модуля
     */
    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
    private List<Lesson> lessons = new ArrayList<>();

    /**
     * Квиз модуля
     */
    @OneToOne(mappedBy = "module", fetch = FetchType.LAZY)
    private Quiz quiz;
}
