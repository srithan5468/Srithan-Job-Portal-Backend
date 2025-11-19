package com.jobportal.backend.repository;

import com.jobportal.backend.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints; // ✅ NEW IMPORT
import jakarta.persistence.QueryHint; // ✅ NEW IMPORT
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    // Add a hint to bypass the second-level cache, forcing a fresh DB read
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "false") })
    List<Job> findByRecruiterId(Long recruiterId);
}