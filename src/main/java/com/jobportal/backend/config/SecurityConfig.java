package com.jobportal.backend.config;

import com.jobportal.backend.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// CORS imports
import org.springframework.web.cors.CorsConfiguration; // ✅ NEW IMPORT
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // ✅ NEW IMPORT
import org.springframework.web.filter.CorsFilter; // ✅ NEW IMPORT

// Assuming JwtAuthenticationFilter is also in this package based on the last fix
import com.jobportal.backend.config.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // ✅ DEFINITIVE CORS FIX: Global CorsFilter Bean
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow all headers, methods, and origins (for development)
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        // 👑 Role-Based Access Control (RBAC) Implemented
        http.authorizeHttpRequests(auth -> auth

                // Allow OPTIONS requests (this is often redundant with the CorsFilter bean, but good for safety)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Public/Auth routes
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/jobs").permitAll() // Allow everyone to see all jobs
                .requestMatchers(HttpMethod.GET, "/jobs/{id}").permitAll() // Allow everyone to see a single job

                // RESTRICTED: Only Recruiters and Admins can create/delete jobs
                .requestMatchers(HttpMethod.POST, "/jobs/create")
                .hasAnyRole("ADMIN", "RECRUITER")
                .requestMatchers(HttpMethod.DELETE, "/jobs/{id}")
                .hasAnyRole("ADMIN", "RECRUITER")

                // RESTRICTED: Only Job Seekers can apply for jobs
                .requestMatchers("/apply/**")
                .hasRole("JOB_SEEKER")

                // Protected routes - Requires any authenticated user (token)
                .requestMatchers("/user/me").authenticated()

                // All other requests must be authenticated by default
                .anyRequest().authenticated()
        );

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authenticationProvider(authenticationProvider());

        // Ensure the custom JWT filter is still applied
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}