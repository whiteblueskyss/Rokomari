package com.learn.mediconnect.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CookieAuthenticationFilter cookieAuthenticationFilter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(cookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                // Allow CORS preflight requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Public endpoints - anyone can access
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/patients/register").permitAll() // Patient registration
                
                // GET methods - everyone authenticated can access
                .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
                
                // Admin - full CRUD access
                .requestMatchers("/api/specializations/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/doctors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/doctors/**").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.DELETE, "/api/doctors/**").hasRole("ADMIN")
                
                .requestMatchers(HttpMethod.POST, "/api/patients/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/patients/**").hasAnyRole("ADMIN", "PATIENT")
                .requestMatchers(HttpMethod.DELETE, "/api/patients/**").hasRole("ADMIN")
                
                .requestMatchers(HttpMethod.DELETE, "/api/appointments/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/prescriptions/**").hasRole("ADMIN")
                
                // Doctor specific permissions
                .requestMatchers(HttpMethod.POST, "/api/prescriptions/**").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.PUT, "/api/prescriptions/**").hasAnyRole("ADMIN", "DOCTOR")
                
                // Patient specific permissions
                .requestMatchers(HttpMethod.POST, "/api/appointments/**").hasAnyRole("ADMIN", "PATIENT")
                .requestMatchers(HttpMethod.PUT, "/api/appointments/**").hasAnyRole("ADMIN", "PATIENT")
                
                // Deny all other requests
                .anyRequest().denyAll()
            );
        
        return http.build();
    }
}
