package com.learningplatform.repository;

import com.learningplatform.entity.Category;
import com.learningplatform.entity.Course;
import com.learningplatform.entity.Lesson;
import com.learningplatform.entity.Module;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class LazyLoadingIT {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Test
    void accessingLazyCollectionOutsideTransaction_shouldFail() {
        // given
        Category category = new Category();
        category.setName("Lazy");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Hibernate Lazy Loading");
        course.setCategory(category);
        course = courseRepository.save(course);

        Module module = new Module();
        module.setTitle("Module 1");
        module.setOrderIndex(1);
        module.setCourse(course);
        module = moduleRepository.save(module);

        Lesson lesson = new Lesson();
        lesson.setTitle("Lesson 1");
        lesson.setModule(module);
        lessonRepository.save(lesson);

        Long courseId = course.getId();

        // when / then
        Course detachedCourse = courseRepository.findById(courseId).orElseThrow();

        assertThrows(
                LazyInitializationException.class,
                () -> detachedCourse.getModules().size()
        );
    }
}
