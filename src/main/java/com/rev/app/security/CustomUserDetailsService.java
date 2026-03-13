package com.rev.app.security;

import com.rev.app.entity.User;
import com.rev.app.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String normalizedEmail = email.toLowerCase().trim();
        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + normalizedEmail));

        log.debug("Found user: {} with status: {}", normalizedEmail, user.getIsActive());

        if (user.getIsActive() == 0) {
            log.warn("Login blocked: User account is inactive for {}", normalizedEmail);
            throw new UsernameNotFoundException("User account is inactive");
        }

        // Enforce portal-specific login: each portal only accepts its matching role
        String portalRole = request.getParameter("portalRole");
        String userRole = user.getRole().name();

        log.info("LOGIN ATTEMPT: email=[{}], portalRole=[{}], userRole=[{}]", normalizedEmail, portalRole, userRole);
        log.debug("Stored hash for {}: [{}]", normalizedEmail, user.getPassword());

        if (portalRole != null && !portalRole.isEmpty()) {
            if (!userRole.equals(portalRole)) {
                log.warn("ROLE MISMATCH: Portal expects {} but user has {}", portalRole, userRole);
                throw new UsernameNotFoundException(
                        "Access denied: This portal is for " + portalRole + " only. Your role is " + userRole + ".");
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }
}
