package com.learningplatform.repository;

import com.learningplatform.entity.User;
import com.learningplatform.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_and_findByEmail_success() {
        User u = new User();
        u.setName("Alice");
        u.setEmail("alice@test.com");
        u.setRole(UserRole.STUDENT);

        userRepository.save(u);

        assertThat(userRepository.findByEmail("alice@test.com")).isPresent();
        assertThat(userRepository.existsByEmail("alice@test.com")).isTrue();
    }

    @Test
    void save_duplicateEmail_shouldFail() {
        User u1 = new User();
        u1.setName("Bob");
        u1.setEmail("dup@test.com");
        u1.setRole(UserRole.STUDENT);
        userRepository.save(u1);

        User u2 = new User();
        u2.setName("Bob2");
        u2.setEmail("dup@test.com");
        u2.setRole(UserRole.TEACHER);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(u2); // flush обязателен, чтобы поймать constraint
        });
    }
}