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
        
        // Update fields only if they are provided (not null)
        if (updatedPatient.getName() != null && !updatedPatient.getName().trim().isEmpty()) {
            existingPatient.setName(updatedPatient.getName());
        }
        if (updatedPatient.getEmail() != null && !updatedPatient.getEmail().trim().isEmpty()) {
            existingPatient.setEmail(updatedPatient.getEmail());
        }
        if (updatedPatient.getPhone() != null && !updatedPatient.getPhone().trim().isEmpty()) {
            existingPatient.setPhone(updatedPatient.getPhone());
        }
        if (updatedPatient.getUsername() != null && !updatedPatient.getUsername().trim().isEmpty()) {
            existingPatient.setUsername(updatedPatient.getUsername());
        }
        if (updatedPatient.getPic() != null && !updatedPatient.getPic().trim().isEmpty()) {
            existingPatient.setPic(updatedPatient.getPic());
        }
        if (updatedPatient.getAge() != null) {
            existingPatient.setAge(updatedPatient.getAge());
        }
        if (updatedPatient.getGender() != null && !updatedPatient.getGender().trim().isEmpty()) {
            existingPatient.setGender(updatedPatient.getGender());
        }
        if (updatedPatient.getAddress() != null && !updatedPatient.getAddress().trim().isEmpty()) {
            existingPatient.setAddress(updatedPatient.getAddress());
        }
        
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
        
        // Check for duplicate email only if email is being updated
        if (updatedPatient.getEmail() != null && !updatedPatient.getEmail().trim().isEmpty() &&
            !existingPatient.getEmail().equals(updatedPatient.getEmail()) && 
            patientRepository.existsByEmail(updatedPatient.getEmail())) {
            throw new IllegalArgumentException("Patient with this email already exists");
        }
        
        // Check for duplicate username only if username is being updated
        if (updatedPatient.getUsername() != null && !updatedPatient.getUsername().trim().isEmpty() &&
            !existingPatient.getUsername().equals(updatedPatient.getUsername()) && 
            patientRepository.existsByUsername(updatedPatient.getUsername())) {
            throw new IllegalArgumentException("Patient with this username already exists");
        }
        
        // Validate age if provided
        if (updatedPatient.getAge() != null && (updatedPatient.getAge() < 0 || updatedPatient.getAge() > 150)) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
    }
}