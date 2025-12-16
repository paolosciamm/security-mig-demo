package com.example.demo.service;

import com.example.demo.dto.RegistrationRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username già esistente");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email già registrata");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        Set<String> roles = new HashSet<>();
        roles.add("USER");
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public void promoteToAdmin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getRoles().add("ADMIN");
        userRepository.save(user);
    }
}