package com.learn.mediconnect.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    
    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
    
    public boolean isDoctor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));
    }
    
    public boolean isPatient() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"));
    }
    
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }
    
    public boolean canAccessDoctorProfile(String doctorUsername) {
        return isAdmin() || (isDoctor() && doctorUsername.equals(getCurrentUsername()));
    }
    
    public boolean canAccessPatientProfile(String patientUsername) {
        return isAdmin() || (isPatient() && patientUsername.equals(getCurrentUsername()));
    }
    
    public boolean canCreatePrescription() {
        return isAdmin() || isDoctor();
    }
    
    public boolean canCreateAppointment() {
        return isAdmin() || isPatient();
    }
    
    public boolean canUpdateAppointment(String patientUsername) {
        return isAdmin() || (isPatient() && patientUsername.equals(getCurrentUsername()));
    }
}