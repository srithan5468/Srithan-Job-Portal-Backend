package com.jobportal.backend.service;

import com.jobportal.backend.model.Application;
import com.jobportal.backend.model.User; // ✅ NEW IMPORT
import com.jobportal.backend.repository.ApplicationRepository;
import com.jobportal.backend.repository.UserRepository; // ✅ NEW IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    // ✅ NEW: Inject UserRepository to securely find user ID by email
    @Autowired
    private UserRepository userRepository;

    // ✅ SECURED: Accepts userEmail instead of an unverified userId
    public Application applyForJob(Long jobId, String userEmail, String resumeUrl) {

        // 1. Securely find the User
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found for application."));

        // 2. Use the securely retrieved userId
        Application application = new Application();
        application.setJobId(jobId);
        application.setUserId(user.getId()); // Use the secure ID from the DB
        application.setResumeUrl(resumeUrl);

        return applicationRepository.save(application);
    }

    public List<Application> getApplications(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }
}