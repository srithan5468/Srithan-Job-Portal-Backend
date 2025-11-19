package com.jobportal.backend.controller;

import com.jobportal.backend.model.Application;
import com.jobportal.backend.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruiter")
@CrossOrigin(origins = "*")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    // 👑 Restricted by default (must be authenticated)
    @GetMapping("/applications")
    public List<Application> getApplications(Authentication authentication) {

        // Get the recruiter's email from the validated token
        String recruiterEmail = authentication.getName();

        // Service handles finding applications based on that email
        return recruiterService.getApplicationsByRecruiter(recruiterEmail);
    }
}