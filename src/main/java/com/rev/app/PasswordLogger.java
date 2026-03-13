package com.rev.app;

import com.rev.app.entity.User;
import com.rev.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class PasswordLogger implements CommandLineRunner {

    private final UserRepository userRepository;

    public PasswordLogger(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("================= USER PASSWORD CHECK =================");
        Optional<User> user = userRepository.findByEmailIgnoreCase("harsha@rev.com");
        if (user.isPresent()) {
            System.out.println("Harsha found! Password Hash: " + user.get().getPassword());
            System.out.println("Active Status: " + user.get().getIsActive());
            System.out.println("Role: " + user.get().getRole());
        } else {
            System.out.println("Harsha not found in the database. Did you create him in this session?");

            System.out.println("\nAll users in DB:");
            userRepository.findAll().forEach(u -> System.out
                    .println("User: " + u.getEmail() + " | Role: " + u.getRole() + " | Active: " + u.getIsActive()));
        }
        System.out.println("=======================================================");
    }
}
