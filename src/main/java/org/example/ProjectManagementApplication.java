package org.example;

import org.example.dao.UserRepository;
import org.example.entities.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ProjectManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectManagementApplication.class, args);
    }

    @Bean
    @Transactional
    public CommandLineRunner seedAdminUser(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            try {
                // Check if admin user exists
                if (!userRepository.existsByUsername("admin")) {
                    User adminUser = new User();
                    adminUser.setUsername("admin");
                    adminUser.setEmail("admin@example.com");
                    adminUser.setPassword(passwordEncoder.encode("admin123"));
                    adminUser.setEnabled(true);

                    // Set admin role
                    Set<String> roles = new HashSet<>();
                    roles.add("ADMIN");
                    adminUser.setRoles(roles);

                    userRepository.save(adminUser);
                    System.out.println("Admin user created successfully!");
                } else {
                    System.out.println("Admin user already exists!");
                }
            } catch (Exception e) {
                System.err.println("Error creating admin user: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}