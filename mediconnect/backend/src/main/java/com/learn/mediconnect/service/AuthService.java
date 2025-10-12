package com.learn.mediconnect.service;

import com.learn.mediconnect.entity.Doctor;
import com.learn.mediconnect.entity.Patient;
import com.learn.mediconnect.repository.DoctorRepository;
import com.learn.mediconnect.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Value("${SUPER_ADMIN_USERNAME:admin}")
    private String adminUsername;
    
    @Value("${SUPER_ADMIN_PASSWORD:admin123}")
    private String adminPassword;
    
    public boolean validateAdminCredentials(String username, String password) {
        return adminUsername.equals(username) && adminPassword.equals(password);
    }
    
    public boolean validateDoctorCredentials(String username, String password) {
        Doctor doctor = doctorRepository.findByUsername(username);
        if (doctor != null) {
            return password.equals(doctor.getPassword());
        }
        return false;
    }
    
    public boolean validatePatientCredentials(String username, String password) {
        Patient patient = patientRepository.findByUsername(username);
        if (patient != null) {
            return password.equals(patient.getPassword());
        }
        return false;
    }
    
    public Doctor getDoctorByUsername(String username) {
        return doctorRepository.findByUsername(username);
    }
    
    public Patient getPatientByUsername(String username) {
        return patientRepository.findByUsername(username);
    }
    
    public Patient registerPatient(com.learn.mediconnect.dto.PatientDTO patientDTO) {
        // Validate required fields
        if (patientDTO.getPassword() == null || patientDTO.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }
        
        // Check if email already exists
        if (patientRepository.existsByEmail(patientDTO.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        
        // Check if username already exists
        if (patientRepository.existsByUsername(patientDTO.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        
        // Generate new ID (manual assignment as per entity design)
        // Find the maximum existing ID and add 1, ensuring uniqueness
        Long nextId;
        do {
            nextId = patientRepository.findAll().stream()
                .mapToLong(Patient::getId)
                .max()
                .orElse(0L) + 1;
        } while (patientRepository.existsById(nextId));
        
        // Create new patient entity
        Patient patient = new Patient();
        patient.setId(nextId);
        patient.setName(patientDTO.getName());
        patient.setEmail(patientDTO.getEmail());
        patient.setUsername(patientDTO.getUsername());
        patient.setPassword(patientDTO.getPassword()); // In production, this should be hashed
        patient.setPhone(patientDTO.getPhone());
        patient.setAge(patientDTO.getAge());
        patient.setGender(patientDTO.getGender());
        patient.setAddress(patientDTO.getAddress());
        patient.setPic(patientDTO.getPic());
        
        return patientRepository.save(patient);
    }
}