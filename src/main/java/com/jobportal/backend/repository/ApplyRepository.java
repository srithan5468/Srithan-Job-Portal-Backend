package com.jobportal.backend.repository;

import com.jobportal.backend.model.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
}
