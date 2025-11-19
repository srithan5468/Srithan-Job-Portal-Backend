package com.jobportal.backend.service;

import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.User;             // ✅ NEW IMPORT
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository; // ✅ NEW IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired // ✅ NEW: Inject UserRepository to look up the user ID
    private UserRepository userRepository;

    // ✅ MODIFIED: Now accepts the recruiterEmail
    public Job createJob(Job job, String recruiterEmail) {

        // 1. Securely find the User
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found."));

        // 2. Set the recruiterId before saving
        job.setRecruiterId(recruiter.getId());

        return jobRepository.save(job);
    }

    // Get all jobs
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Get job by ID
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    // Delete job
    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new RuntimeException("Job not found");
        }
        jobRepository.deleteById(id);
    }
}