package com.jobportal.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement; // ✅ NEW IMPORT

@SpringBootApplication
@EnableTransactionManagement // ✅ NEW ANNOTATION: Enables @Transactional usage
public class JobportalBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobportalBackendApplication.class, args);
    }
}