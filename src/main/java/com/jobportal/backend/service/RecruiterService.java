package com.jobportal.backend.service;

import com.jobportal.backend.model.Application;
import com.jobportal.backend.model.Job;
import com.jobportal.backend.repository.ApplicationRepository; // ✅ MISSING
import com.jobportal.backend.repository.JobRepository; // ✅ MISSING
import com.jobportal.backend.repository.UserRepository; // ✅ MISSING
import org.springframework.beans.factory.annotation.Autowired; // ✅ MISSING
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ MISSING
import jakarta.persistence.EntityManager; // ✅ MISSING

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RecruiterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EntityManager entityManager;

    // Fetches all applications for all jobs posted by the given recruiterEmail
    public List<Application> getApplicationsByRecruiter(String recruiterEmail) {

        // --- Synchronization Fix ---
        // Force the persistence context to check for recent committed changes
        entityManager.flush();
        entityManager.clear();
        // ---------------------------

        // 1. Get the recruiter's ID securely
        Long recruiterId = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found")).getId();

        // 2. Find all job IDs posted by this recruiter
        List<Job> jobs = jobRepository.findByRecruiterId(recruiterId);
        List<Long> jobIds = jobs.stream().map(Job::getId).collect(Collectors.toList());

        // 3. Find all applications for those jobs
        return jobIds.stream()
                .flatMap(jobId -> applicationRepository.findByJobId(jobId).stream())
                .collect(Collectors.toList());
    }
}