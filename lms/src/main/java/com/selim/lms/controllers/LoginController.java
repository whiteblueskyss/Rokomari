package com.selim.lms.controllers;

import com.selim.lms.dto.UserDto;
import com.selim.lms.services.UserService;
import com.selim.lms.utils.AuthUtil;
import com.selim.lms.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private AuthUtil authUtil;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Find user by ID
            UserDto userDto = userService.getById(loginRequest.getUserId());
            
            // Generate JWT token
            String token = jwtTokenUtil.generateToken(userDto.getId());
            
            // Create HTTP-only cookie with JWT token
            ResponseCookie jwtCookie = ResponseCookie.from("jwt-token", token)
                    .httpOnly(true)           // Prevents JavaScript access (XSS protection)
                    .secure(false)            // Set to true in production with HTTPS
                    .sameSite("Strict")       // CSRF protection
                    .maxAge(Duration.ofHours(24)) // 24 hours (same as token expiry)
                    .path("/")                // Available for all paths
                    .build();
            
            // Return response with cookie
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);  // Still return token in response for testing
            response.put("userId", userDto.getId());
            response.put("userName", userDto.getName());
            response.put("userEmail", userDto.getEmail());
            response.put("userRole", userDto.getRole().toString());
            response.put("message", "Login successful - JWT token set in cookie");
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }
    
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "jwt-token", required = false) String cookieToken) {
        try {
            String token = null;
            
            // Try to get token from Authorization header first
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7); // Remove "Bearer " prefix
            }
            // If no header token, try to get from cookie
            else if (cookieToken != null && !cookieToken.isEmpty()) {
                token = cookieToken;
            }
            
            if (token == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "No token found in Authorization header or cookie"));
            }
            
            if (!jwtTokenUtil.isTokenValid(token)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token"));
            }
            
            if (jwtTokenUtil.isTokenExpired(token)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Token expired"));
            }
            
            Long userId = jwtTokenUtil.getUserIdFromToken(token);
            UserDto userDto = userService.getById(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("userId", userDto.getId());
            response.put("userName", userDto.getName());
            response.put("userEmail", userDto.getEmail());
            response.put("userRole", userDto.getRole().toString());
            response.put("tokenSource", authHeader != null ? "header" : "cookie");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "Token validation failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Create cookie with same name but expired (maxAge = 0)
        ResponseCookie expiredCookie = ResponseCookie.from("jwt-token", "")
                .httpOnly(true)
                .secure(false)  // Set to true in production with HTTPS
                .sameSite("Strict")
                .maxAge(Duration.ofSeconds(0)) // Expire immediately
                .path("/")
                .build();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Logged out successfully - JWT cookie cleared");
        
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
                .body(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserInfo(HttpServletRequest request) {
        UserDto currentUser = authUtil.getCurrentUser(request);
        
        if (currentUser == null) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Unauthorized - No valid token found"));
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", currentUser.getId());
        response.put("userName", currentUser.getName());
        response.put("userEmail", currentUser.getEmail());
        response.put("userRole", currentUser.getRole().toString());
        response.put("isAdmin", authUtil.isAdmin(request));
        response.put("isReader", authUtil.isReader(request));
        response.put("isAuthenticated", true);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/admin-only")
    public ResponseEntity<?> adminOnlyEndpoint(HttpServletRequest request) {
        if (!authUtil.isAuthenticated(request)) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Unauthorized - Please login first"));
        }
        
        if (!authUtil.isAdmin(request)) {
            return ResponseEntity.status(403)
                .body(Map.of("error", "Forbidden - Admin access required"));
        }
        
        UserDto currentUser = authUtil.getCurrentUser(request);
        return ResponseEntity.ok(Map.of(
            "message", "Welcome Admin! This is an admin-only endpoint",
            "user", currentUser.getName(),
            "role", currentUser.getRole().toString()
        ));
    }
    
    @GetMapping("/reader-only")
    public ResponseEntity<?> readerOnlyEndpoint(HttpServletRequest request) {
        if (!authUtil.isAuthenticated(request)) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Unauthorized - Please login first"));
        }
        
        if (!authUtil.isReader(request)) {
            return ResponseEntity.status(403)
                .body(Map.of("error", "Forbidden - Reader access required"));
        }
        
        UserDto currentUser = authUtil.getCurrentUser(request);
        return ResponseEntity.ok(Map.of(
            "message", "Welcome Reader! This is a reader-only endpoint",
            "user", currentUser.getName(),
            "role", currentUser.getRole().toString()
        ));
    }
    
    @GetMapping("/any-authenticated")
    public ResponseEntity<?> anyAuthenticatedEndpoint(HttpServletRequest request) {
        if (!authUtil.isAuthenticated(request)) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Unauthorized - Please login first"));
        }
        
        UserDto currentUser = authUtil.getCurrentUser(request);
        return ResponseEntity.ok(Map.of(
            "message", "Welcome! This endpoint is for any authenticated user",
            "user", currentUser.getName(),
            "role", currentUser.getRole().toString(),
            "isAdmin", authUtil.isAdmin(request),
            "isReader", authUtil.isReader(request)
        ));
    }
    
    // Inner class for login request
    public static class LoginRequest {
        private Long userId;
        
        public Long getUserId() {
            return userId;
        }
        
        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }
}