package com.jobportal.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "job")
@Data
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ NEW FIELD: Track the user who created this job
    private Long recruiterId;

    private String title;
    private String description;
    private String salary;
    private String company;
}