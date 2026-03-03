package com.rev.app.repository;

import com.rev.app.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmailIgnoreCase_ExistingUser_ReturnsUser() {
        User user = new User();
        user.setEmail("test@rev.com");
        user.setPassword("hashedPassword");
        user.setRole(User.Role.ROLE_EMPLOYEE);
        user.setIsActive(1);
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmailIgnoreCase("TEST@REV.COM");

        assertTrue(found.isPresent());
        assertEquals("test@rev.com", found.get().getEmail());
    }

    @Test
    void testFindByEmailIgnoreCase_NonExistingUser_ReturnsEmpty() {
        Optional<User> found = userRepository.findByEmailIgnoreCase("ghost@rev.com");
        assertFalse(found.isPresent());
    }

    @Test
    void testExistsByEmailIgnoreCase_ExistingUser_ReturnsTrue() {
        User user = new User();
        user.setEmail("exists@rev.com");
        user.setPassword("pwd");
        user.setRole(User.Role.ROLE_MANAGER);
        user.setIsActive(1);
        userRepository.save(user);

        assertTrue(userRepository.existsByEmailIgnoreCase("EXISTS@REV.COM"));
    }

    @Test
    void testSaveAndFindById_ReturnsCorrectUser() {
        User user = new User();
        user.setEmail("find_me@rev.com");
        user.setPassword("pwd");
        user.setRole(User.Role.ROLE_ADMIN);
        user.setIsActive(1);
        User saved = userRepository.save(user);

        Optional<User> found = userRepository.findById(saved.getUserId());
        assertTrue(found.isPresent());
        assertEquals("find_me@rev.com", found.get().getEmail());
    }
}
