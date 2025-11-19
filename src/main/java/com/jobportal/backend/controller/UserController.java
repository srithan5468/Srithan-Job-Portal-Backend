package com.jobportal.backend.controller;

import com.jobportal.backend.model.User;
import com.jobportal.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Protected API
    @GetMapping("/me")
    public User getLoggedInUser(Authentication authentication) {
        String email = authentication.getName();  // Extracted from JWT token
        return userService.getByEmail(email);
    }
}
