package com.jobportal.backend.controller;

import com.jobportal.backend.model.Application;
import com.jobportal.backend.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // ✅ NEW IMPORT
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apply")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    // ✅ SECURED: userId removed from the path/request
    @PostMapping("/{jobId}")
    public Application applyForJob(
            @PathVariable Long jobId,
            @RequestParam String resumeUrl,
            Authentication authentication // ✅ Inject current user's security details
    ) {
        String userEmail = authentication.getName(); // Get the email (principal) from the token

        // Pass email to the service layer for secure ID lookup
        return applicationService.applyForJob(jobId, userEmail, resumeUrl);
    }

    @GetMapping("/{jobId}")
    public List<Application> getApplications(@PathVariable Long jobId) {
        return applicationService.getApplications(jobId);
    }
}