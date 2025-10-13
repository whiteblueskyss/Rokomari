package com.learn.mediconnect.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.learn.mediconnect.service.SessionValidationService;

import java.io.IOException;
import java.util.Collections;

@Component
public class CookieAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private SessionValidationService sessionValidationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Skip authentication for auth endpoints (except validate-session)
        String requestURI = request.getRequestURI();
        if ((requestURI.startsWith("/api/auth/") && !requestURI.equals("/api/auth/validate-session")) 
            || requestURI.startsWith("/api/patients/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get userSession cookie
        String userSession = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userSession".equals(cookie.getName())) {
                    userSession = cookie.getValue();
                    break;
                }
            }
        }

        if (userSession != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Validate session and extract user info
            String[] sessionParts = userSession.split("_", 2);
            if (sessionParts.length == 2) {
                String userType = sessionParts[0];
                String username = sessionParts[1];
                
                // Validate session
                if (sessionValidationService.isValidSession(userType, username)) {
                    // Create authentication token with role
                    String role = "ROLE_" + userType.toUpperCase();
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                    
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(username, null, 
                                                              Collections.singletonList(authority));
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}