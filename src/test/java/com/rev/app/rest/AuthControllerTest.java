package com.rev.app.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rev.app.dto.ApiRegistrationRequest;
import com.rev.app.dto.AuthRequest;
import com.rev.app.entity.User.Role;
import com.rev.app.entity.User;
import com.rev.app.security.JwtUtil;
import com.rev.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AuthenticationManager authenticationManager;

        @MockBean
        private JwtUtil jwtUtil;

        @MockBean
        private UserService userService;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
        }

        @Test
        void testLoginSuccess() throws Exception {
                AuthRequest req = new AuthRequest("test@test.com", "pass123");
                Authentication auth = mock(Authentication.class);
                org.springframework.security.core.userdetails.User springUser = new org.springframework.security.core.userdetails.User(
                                "test@test.com", "pass123", java.util.Collections.emptyList());

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(auth);
                when(auth.getPrincipal()).thenReturn(springUser);
                when(jwtUtil.generateToken(any())).thenReturn("token123");

                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").value("token123"))
                                .andExpect(jsonPath("$.email").value("test@test.com"));
        }

        @Test
        void testLoginFailure() throws Exception {
                AuthRequest req = new AuthRequest("test@test.com", "wrongpass");

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenThrow(new BadCredentialsException("Bad credentials"));

                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                                .andExpect(status().isUnauthorized())
                                .andExpect(jsonPath("$.error").exists());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void testRegister() throws Exception {
                ApiRegistrationRequest req = new ApiRegistrationRequest();
                req.setEmail("newuser@test.com");
                req.setPassword("pass12345");
                req.setFirstName("New");
                req.setLastName("User");
                req.setRole("ROLE_EMPLOYEE");

                User createdUser = new User();
                createdUser.setEmail("newuser@test.com");
                createdUser.setRole(Role.ROLE_EMPLOYEE);

                when(userService.registerUser(any())).thenReturn(createdUser);

                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.email").value("newuser@test.com"));
        }
}
