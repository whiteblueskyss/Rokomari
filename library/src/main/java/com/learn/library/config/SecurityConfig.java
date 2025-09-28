package com.learn.library.config;

import org.springframework.context.annotation.Bean; // this import is for @Bean annotation. It indicates that a method produces a bean to be managed by the Spring container. 
import org.springframework.context.annotation.Configuration; // this import is for @Configuration annotation. It indicates that the class has @Bean definition methods. 
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // this import is for HttpSecurity class. It allows configuring web based security for specific http requests.  
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // this import is for BCryptPasswordEncoder class. It is used to hash passwords using the BCrypt strong hashing function.
import org.springframework.security.crypto.password.PasswordEncoder; // this import is for PasswordEncoder interface. It is used to perform password encoding and matching.
import org.springframework.security.provisioning.InMemoryUserDetailsManager; // this import is for InMemoryUserDetailsManager class. It is an implementation of UserDetailsService that stores user details in memory.
import org.springframework.security.core.userdetails.User; // this import is for User class. It is a core user information implementation that is used by UserDetailsService.
import org.springframework.security.core.userdetails.UserDetails; // this import is for UserDetails interface. It provides core user information.
import org.springframework.security.web.SecurityFilterChain; // this import is for SecurityFilterChain interface. It is used to configure the security filter chain.
import org.springframework.http.HttpMethod; // this import is for HttpMethod enum. It represents HTTP methods such as GET, POST, PUT, DELETE, etc.

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
