package com.learningplatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {
        "module",
        "assignments"
})
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;

    /**
     * Содержимое урока (текст, ссылка и т.п.)
     */
    @Column(length = 4000)
    private String content;

    /**
     * Модуль, к которому относится урок
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private Module module;

    /**
     * Задания урока
     */
    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY)
    private List<Assignment> assignments = new ArrayList<>();
}
