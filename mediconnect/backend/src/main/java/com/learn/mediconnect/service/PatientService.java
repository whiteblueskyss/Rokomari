package com.learn.mediconnect.service;

import com.learn.mediconnect.entity.Patient;
import com.learn.mediconnect.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient createPatient(Patient patient) {
        validatePatientForCreation(patient);
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existingPatient = getPatientById(id);
        validatePatientForUpdate(existingPatient, updatedPatient);
        
        // Update fields
        existingPatient.setName(updatedPatient.getName());
        existingPatient.setEmail(updatedPatient.getEmail());
        existingPatient.setPhone(updatedPatient.getPhone());
        existingPatient.setUsername(updatedPatient.getUsername());
        existingPatient.setPic(updatedPatient.getPic());
        existingPatient.setAge(updatedPatient.getAge());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setAddress(updatedPatient.getAddress());
        
        // Only update password if provided
        if (updatedPatient.getPassword() != null && !updatedPatient.getPassword().trim().isEmpty()) {
            existingPatient.setPassword(updatedPatient.getPassword());
        }
        
        return patientRepository.save(existingPatient);
    }


    @Transactional(readOnly = true)
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + id));
    }


    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }


    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("Patient not found with ID: " + id);
        }
        patientRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return patientRepository.existsByEmail(email);
    }



    @Transactional(readOnly = true)
    public boolean isUsernameExists(String username) {
        return patientRepository.existsByUsername(username);
    }


    private void validatePatientForCreation(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        
        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name is required");
        }
        
        if (patient.getEmail() == null || patient.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (patient.getUsername() == null || patient.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        if (patient.getPassword() == null || patient.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        // Check for duplicate email
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new IllegalArgumentException("Patient with this email already exists");
        }
        
        // Check for duplicate username
        if (patientRepository.existsByUsername(patient.getUsername())) {
            throw new IllegalArgumentException("Patient with this username already exists");
        }
        
        // Validate age if provided
        if (patient.getAge() != null && (patient.getAge() < 0 || patient.getAge() > 150)) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
    }


    private void validatePatientForUpdate(Patient existingPatient, Patient updatedPatient) {
        if (updatedPatient == null) {
            throw new IllegalArgumentException("Updated patient data cannot be null");
        }
        
        if (updatedPatient.getName() == null || updatedPatient.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name is required");
        }
        
        if (updatedPatient.getEmail() == null || updatedPatient.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (updatedPatient.getUsername() == null || updatedPatient.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        // Check for duplicate email (excluding current patient)
        if (!existingPatient.getEmail().equals(updatedPatient.getEmail()) && 
            patientRepository.existsByEmail(updatedPatient.getEmail())) {
            throw new IllegalArgumentException("Patient with this email already exists");
        }
        
        // Check for duplicate username (excluding current patient)
        if (!existingPatient.getUsername().equals(updatedPatient.getUsername()) && 
            patientRepository.existsByUsername(updatedPatient.getUsername())) {
            throw new IllegalArgumentException("Patient with this username already exists");
        }
        
        // Validate age if provided
        if (updatedPatient.getAge() != null && (updatedPatient.getAge() < 0 || updatedPatient.getAge() > 150)) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
    }
}