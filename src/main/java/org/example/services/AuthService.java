package org.example.services;

import org.example.dao.UserRepository;
import org.example.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default role if none provided
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Set<String> roles = new HashSet<>();
            roles.add("USER");
            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
} 