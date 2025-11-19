package com.jobportal.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long jobId;

    private String resumeUrl;
}
