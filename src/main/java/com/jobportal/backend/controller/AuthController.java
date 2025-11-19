package com.jobportal.backend.controller;

import com.jobportal.backend.dto.LoginRequest;
import com.jobportal.backend.model.User;
import com.jobportal.backend.service.JwtService;
import com.jobportal.backend.service.UserDetailsServiceImpl;
import com.jobportal.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // ✅ FIXED: Autowire the PasswordEncoder interface bean from SecurityConfig
    @Autowired
    private PasswordEncoder encoder;

    @PostConstruct
    public void init() {
        System.out.println("✅ AuthController initialized successfully!");
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        System.out.println("🔹 Registering user: " + user.getEmail());
        return userService.register(user);
    }

    // ✅ LOGIN
    // 🛠️ FIXED: Added produces = "text/plain" to ensure the raw token string is returned,
    // which fixes the frontend's inability to store the JWT.
    @PostMapping(value = "/login", produces = "text/plain")
    public String login(@RequestBody LoginRequest request) {
        System.out.println("🔹 Login request for email: " + request.getEmail());

        User user = userService.getByEmail(request.getEmail());
        if (user == null) {
            System.out.println("❌ User not found in DB: " + request.getEmail());
            // Returning errors as strings here is okay for simple APIs, but returning a
            // custom error status/response body is better practice.
            return "User not found";
        }

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println("❌ Invalid password for user: " + request.getEmail());
            return "Invalid credentials";
        }

        // Generate token upon successful authentication
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);

        System.out.println("✅ Login successful for: " + user.getEmail());
        System.out.println("🔑 Token generated: " + token);

        return token;
    }
}