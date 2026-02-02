package com.learningplatform.repository;

import com.learningplatform.entity.Category;
import com.learningplatform.entity.Course;
import com.learningplatform.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TagRepositoryIT {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void course_canHaveMultipleTags() {
        // given
        Category category = new Category();
        category.setName("Programming");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Java Hibernate");
        course.setCategory(category);
        course = courseRepository.save(course);

        Tag tag1 = new Tag();
        tag1.setName("Java");
        tag1 = tagRepository.save(tag1);

        Tag tag2 = new Tag();
        tag2.setName("Hibernate");
        tag2 = tagRepository.save(tag2);

        // when
        course.getTags().add(tag1);
        course.getTags().add(tag2);
        courseRepository.save(course);

        // then
        Course fromDb = courseRepository.findById(course.getId()).orElseThrow();
        assertThat(fromDb.getTags()).hasSize(2);
        assertThat(fromDb.getTags())
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("Java", "Hibernate");
    }
}
