package com.learn.mediconnect.controller;

import com.learn.mediconnect.dto.LoginRequest;
import com.learn.mediconnect.dto.LoginResponse;
import com.learn.mediconnect.dto.PatientDTO;
import com.learn.mediconnect.service.AuthService;
import com.learn.mediconnect.entity.Doctor;
import com.learn.mediconnect.entity.Patient;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/admin")
    public ResponseEntity<LoginResponse> adminLogin(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            if (!authService.validateAdminCredentials(username, password)) {
                LoginResponse errorResponse = LoginResponse.failure("Invalid username or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            Cookie sessionCookie = new Cookie("userSession", "admin_" + username);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setSecure(false); 
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(3600); 
            response.addCookie(sessionCookie);

            LoginResponse successResponse = LoginResponse.success("ADMIN", username);
            return ResponseEntity.ok(successResponse);

        } catch (Exception e) {
            LoginResponse errorResponse = LoginResponse.failure("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/doctor")
    public ResponseEntity<LoginResponse> doctorLogin(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            if (!authService.validateDoctorCredentials(username, password)) {
                LoginResponse errorResponse = LoginResponse.failure("Invalid username or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            Doctor doctor = authService.getDoctorByUsername(username);
            
            Cookie sessionCookie = new Cookie("userSession", "doctor_" + username);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setSecure(false); 
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(3600); 
            response.addCookie(sessionCookie);

            LoginResponse successResponse = LoginResponse.success("DOCTOR", username);
            return ResponseEntity.ok(successResponse);

        } catch (Exception e) {
            LoginResponse errorResponse = LoginResponse.failure("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/patient")
    public ResponseEntity<LoginResponse> patientLogin(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            if (!authService.validatePatientCredentials(username, password)) {
                LoginResponse errorResponse = LoginResponse.failure("Invalid username or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            Patient patient = authService.getPatientByUsername(username);
            
            Cookie sessionCookie = new Cookie("userSession", "patient_" + username);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setSecure(false); 
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(3600); 
            response.addCookie(sessionCookie);

            LoginResponse successResponse = LoginResponse.success("PATIENT", username, patient.getId());
            return ResponseEntity.ok(successResponse);

        } catch (Exception e) {
            LoginResponse errorResponse = LoginResponse.failure("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(HttpServletResponse response) {
        try {
            // Clear the session cookie
            Cookie sessionCookie = new Cookie("userSession", null);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setSecure(false); 
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(0); 
            response.addCookie(sessionCookie);

            LoginResponse successResponse = LoginResponse.success(null, null);
            successResponse.setMessage("Logout successful");
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            LoginResponse errorResponse = LoginResponse.failure("Logout failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> registerPatient(@Valid @RequestBody PatientDTO patientDTO) {
        try {
            // Register the patient
            authService.registerPatient(patientDTO);
            
            LoginResponse successResponse = LoginResponse.success("PATIENT", patientDTO.getUsername());
            successResponse.setMessage("Registration successful! You can now log in.");
            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
            
        } catch (Exception e) {
            LoginResponse errorResponse = LoginResponse.failure("Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/validate-session")
    public ResponseEntity<LoginResponse> validateSession(HttpServletRequest request) {
        try {
            // Get user from SecurityContext (set by CookieAuthenticationFilter)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.isAuthenticated() && 
                !(authentication instanceof AnonymousAuthenticationToken)) {
                
                String username = authentication.getName();
                String fullRole = authentication.getAuthorities().iterator().next().getAuthority();
                // Extract userType from "ROLE_ADMIN" -> "ADMIN"
                String userType = fullRole.startsWith("ROLE_") ? fullRole.substring(5) : fullRole;
                
                // Get user ID based on user type
                Long userId = null;
                if ("PATIENT".equals(userType)) {
                    Patient patient = authService.getPatientByUsername(username);
                    if (patient != null) {
                        userId = patient.getId();
                    }
                } else if ("DOCTOR".equals(userType)) {
                    Doctor doctor = authService.getDoctorByUsername(username);
                    if (doctor != null) {
                        userId = doctor.getId();
                    }
                }
                
                return ResponseEntity.ok(new LoginResponse(true, "Session valid", userType, username, userId));
            }
            
            return ResponseEntity.ok(new LoginResponse(false, "No valid session", null, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new LoginResponse(false, "Session validation failed", null, null));
        }
    }

}