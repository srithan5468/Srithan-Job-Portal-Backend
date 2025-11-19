package com.jobportal.backend.service;

import com.jobportal.backend.model.Apply;
import com.jobportal.backend.repository.ApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    @Autowired
    private ApplyRepository applyRepository;

    public Apply applyJob(Long userId, Long jobId, String resumeUrl) {
        Apply apply = new Apply();
        apply.setUserId(userId);
        apply.setJobId(jobId);
        apply.setResumeUrl(resumeUrl);
        return applyRepository.save(apply);
    }
}
