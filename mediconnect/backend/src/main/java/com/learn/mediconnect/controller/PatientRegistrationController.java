package com.learn.mediconnect.controller;

import com.learn.mediconnect.dto.PatientDTO;
import com.learn.mediconnect.dto.LoginResponse;
import com.learn.mediconnect.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class PatientRegistrationController {

    @Autowired
    private AuthService authService;

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
}