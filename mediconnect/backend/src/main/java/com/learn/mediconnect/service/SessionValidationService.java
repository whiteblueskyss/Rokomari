package com.learn.mediconnect.service;

import com.learn.mediconnect.entity.Doctor;
import com.learn.mediconnect.entity.Patient;
import com.learn.mediconnect.repository.DoctorRepository;
import com.learn.mediconnect.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SessionValidationService {
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Value("${SUPER_ADMIN_USERNAME:admin}")
    private String adminUsername;

    public boolean isValidSession(String userType, String username) {
        switch (userType.toLowerCase()) {
            case "admin":
                return adminUsername.equals(username);
            case "doctor":
                Doctor doctor = doctorRepository.findByUsername(username);
                return doctor != null;
            case "patient":
                Patient patient = patientRepository.findByUsername(username);
                return patient != null;
            default:
                return false;
        }
    }
    
    public String getUserRole(String userType) {
        return "ROLE_" + userType.toUpperCase();
    }
}