package com.learningplatform.repository;

import com.learningplatform.entity.Category;
import com.learningplatform.entity.Course;
import com.learningplatform.entity.User;
import com.learningplatform.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CourseRepositoryIT {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByTeacherId_and_findByCategoryId_success() {
        User teacher = new User();
        teacher.setName("Teacher");
        teacher.setEmail("teacher@test.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        Category cat = new Category();
        cat.setName("Programming");
        cat = categoryRepository.save(cat);

        Course c1 = new Course();
        c1.setTitle("Java");
        c1.setTeacher(teacher);
        c1.setCategory(cat);
        courseRepository.save(c1);

        Course c2 = new Course();
        c2.setTitle("Spring");
        c2.setTeacher(teacher);
        c2.setCategory(cat);
        courseRepository.save(c2);

        List<Course> byTeacher = courseRepository.findByTeacherId(teacher.getId());
        assertThat(byTeacher).hasSize(2);

        List<Course> byCategory = courseRepository.findByCategoryId(cat.getId());
        assertThat(byCategory).hasSize(2);
    }
}
