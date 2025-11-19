package com.jobportal.backend.controller;

import com.jobportal.backend.model.Job;
import com.jobportal.backend.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // ✅ NEW IMPORT
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // ==========================
    // CREATE NEW JOB (PROTECTED)
    // ==========================
    @PostMapping("/create")
    // ✅ MODIFIED: Added Authentication object to get the user's email
    public Job createJob(@RequestBody Job job, Authentication authentication) {

        String recruiterEmail = authentication.getName(); // Get the email (principal) from the token

        // Pass both job details and the recruiter's email to the service
        return jobService.createJob(job, recruiterEmail);
    }

    // ==========================
    // PUBLIC: GET ALL JOBS
    // ==========================
    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    // ==========================
    // GET JOB BY ID (PROTECTED)
    // ==========================
    @GetMapping("/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    // ==========================
    // DELETE JOB (PROTECTED)
    // ==========================
    @DeleteMapping("/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return "Job deleted!";
    }
}