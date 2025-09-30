package com.selim.lms.utils;

import com.selim.lms.dto.UserDto;
import com.selim.lms.entities.Role;
import com.selim.lms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthUtil {
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserService userService;
    
    // Extract JWT token from request (cookie or Authorization header)

    public String extractTokenFromRequest(HttpServletRequest request) {
        // First try Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        
        // Then try cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }
    
    // Get current user from JWT token in request
     
    public UserDto getCurrentUser(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            return null;
        }
        
        try {
            if (!jwtTokenUtil.isTokenValid(token) || jwtTokenUtil.isTokenExpired(token)) {
                return null;
            }
            
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            return userService.getById(userId);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    //Check if current user is Admin
     
    public boolean isAdmin(HttpServletRequest request) {
        UserDto user = getCurrentUser(request);
        return user != null && "ADMIN".equals(user.getRole());
    }
    
    //Check if current user is Reader
    
    public boolean isReader(HttpServletRequest request) {
        UserDto user = getCurrentUser(request);
        return user != null && "READER".equals(user.getRole());
    }

    //Check if user is authenticated (has valid token)
    public boolean isAuthenticated(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }
    
    //Get user role as string
     
    public String getUserRole(HttpServletRequest request) {
        UserDto user = getCurrentUser(request);
        return user != null ? user.getRole().toString() : null;
    }
    
    //Get user ID
     
    public Long getUserId(HttpServletRequest request) {
        UserDto user = getCurrentUser(request);
        return user != null ? user.getId() : null;
    }
}