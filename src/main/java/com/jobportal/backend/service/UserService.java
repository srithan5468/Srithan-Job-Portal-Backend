package com.jobportal.backend.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobportal.backend.model.User;
import com.jobportal.backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User register(User user) {

        // Hash password before saving
        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
