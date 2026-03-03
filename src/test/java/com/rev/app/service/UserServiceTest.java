package com.rev.app.service;

import com.rev.app.entity.User;
import com.rev.app.exceptions.BusinessException;
import com.rev.app.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DesignationRepository designationRepository;

    @Mock
    private LeaveService leaveService;

    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl();
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
        ReflectionTestUtils.setField(userService, "employeeRepository", employeeRepository);
        ReflectionTestUtils.setField(userService, "departmentRepository", departmentRepository);
        ReflectionTestUtils.setField(userService, "designationRepository", designationRepository);
        ReflectionTestUtils.setField(userService, "leaveService", leaveService);
    }

    @Test
    void testCreateUser_NewEmail_CreatesUser() {
        when(userRepository.findByEmailIgnoreCase("newuser@rev.com")).thenReturn(Optional.empty());
        User savedUser = new User();
        savedUser.setEmail("newuser@rev.com");
        savedUser.setRole(User.Role.ROLE_EMPLOYEE);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser("newuser@rev.com", "password123", User.Role.ROLE_EMPLOYEE);

        assertNotNull(result);
        assertEquals("newuser@rev.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_DuplicateEmail_ThrowsBusinessException() {
        User existing = new User();
        existing.setEmail("existing@rev.com");
        when(userRepository.findByEmailIgnoreCase("existing@rev.com")).thenReturn(Optional.of(existing));

        assertThrows(BusinessException.class,
                () -> userService.createUser("existing@rev.com", "password", User.Role.ROLE_EMPLOYEE));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testFindByEmail_Existing_ReturnsUser() {
        User user = new User();
        user.setEmail("test@rev.com");
        when(userRepository.findByEmailIgnoreCase("test@rev.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail("test@rev.com");

        assertTrue(result.isPresent());
        assertEquals("test@rev.com", result.get().getEmail());
    }

    @Test
    void testFindByEmail_NotExisting_ReturnsEmpty() {
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail("ghost@rev.com");

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdatePassword_UserExists_PasswordIsUpdated() {
        User user = new User();
        user.setUserId(1L);
        user.setPassword("oldHash");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updatePassword(1L, "newPassword123");

        verify(userRepository, times(1)).save(argThat(u -> !u.getPassword().equals("oldHash")));
    }
}
