package com.learn.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    // Step 3.1: Define an in-memory user
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("admin")
                .password(passwordEncoder().encode("password"))
                .roles("USER")  // role assigned to this user
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    // Step 3.2: Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Step 3.3: Define which endpoints are public or restricted
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests()
                // Allow anyone to GET /api/**
                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                // Restrict POST, PUT, DELETE to role USER
                .requestMatchers(HttpMethod.POST, "/api/**").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("USER")
                .anyRequest().authenticated()
            .and()
                .httpBasic();
        return http.build();
    }
}
