package com.learn.mediconnect.service;

import com.learn.mediconnect.entity.Doctor;
import com.learn.mediconnect.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }


    public Doctor createDoctor(Doctor doctor) {
        validateDoctorForCreation(doctor);
        return doctorRepository.save(doctor);
    }


    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor existingDoctor = getDoctorById(id);
        validateDoctorForUpdate(existingDoctor, updatedDoctor);
        
        // Update fields only if they are provided (not null)
        if (updatedDoctor.getName() != null && !updatedDoctor.getName().trim().isEmpty()) {
            existingDoctor.setName(updatedDoctor.getName());
        }
        if (updatedDoctor.getEmail() != null && !updatedDoctor.getEmail().trim().isEmpty()) {
            existingDoctor.setEmail(updatedDoctor.getEmail());
        }
        if (updatedDoctor.getUsername() != null && !updatedDoctor.getUsername().trim().isEmpty()) {
            existingDoctor.setUsername(updatedDoctor.getUsername());
        }
        if (updatedDoctor.getPassword() != null && !updatedDoctor.getPassword().trim().isEmpty()) {
            existingDoctor.setPassword(updatedDoctor.getPassword());
        }
        if (updatedDoctor.getPhone() != null && !updatedDoctor.getPhone().trim().isEmpty()) {
            existingDoctor.setPhone(updatedDoctor.getPhone());
        }
        if (updatedDoctor.getSpecializations() != null && !updatedDoctor.getSpecializations().trim().isEmpty()) {
            existingDoctor.setSpecializations(updatedDoctor.getSpecializations());
        }
        if (updatedDoctor.getVisitingDays() != null && !updatedDoctor.getVisitingDays().trim().isEmpty()) {
            existingDoctor.setVisitingDays(updatedDoctor.getVisitingDays());
        }
        
        return doctorRepository.save(existingDoctor);
    }


    @Transactional(readOnly = true)
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + id));
    }


    @Transactional(readOnly = true)
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }


    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new IllegalArgumentException("Doctor not found with ID: " + id);
        }
        doctorRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization cannot be null or empty");
        }
        return doctorRepository.findBySpecialization(specialization.trim());
    }


    @Transactional(readOnly = true)
    public List<Doctor> getDoctorsByVisitingDay(String day) {
        if (day == null || day.trim().isEmpty()) {
            throw new IllegalArgumentException("Day cannot be null or empty");
        }
        return doctorRepository.findByVisitingDays(day.trim());
    }


    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return doctorRepository.existsByEmail(email);
    }


    @Transactional(readOnly = true)
    public boolean isUsernameExists(String username) {
        return doctorRepository.existsByUsername(username);
    }


    private void validateDoctorForCreation(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        
        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name is required");
        }
        
        if (doctor.getEmail() == null || doctor.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (doctor.getUsername() == null || doctor.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        if (doctor.getPassword() == null || doctor.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        // Check for duplicate email
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new IllegalArgumentException("Doctor with this email already exists");
        }
        
        // Check for duplicate username
        if (doctorRepository.existsByUsername(doctor.getUsername())) {
            throw new IllegalArgumentException("Doctor with this username already exists");
        }
    }


    private void validateDoctorForUpdate(Doctor existingDoctor, Doctor updatedDoctor) {
        if (updatedDoctor == null) {
            throw new IllegalArgumentException("Updated doctor data cannot be null");
        }
        
        // Check for duplicate email only if email is being updated
        if (updatedDoctor.getEmail() != null && !updatedDoctor.getEmail().trim().isEmpty() &&
            !existingDoctor.getEmail().equals(updatedDoctor.getEmail()) && 
            doctorRepository.existsByEmail(updatedDoctor.getEmail())) {
            throw new IllegalArgumentException("Doctor with this email already exists");
        }
        
        // Check for duplicate username only if username is being updated
        if (updatedDoctor.getUsername() != null && !updatedDoctor.getUsername().trim().isEmpty() &&
            !existingDoctor.getUsername().equals(updatedDoctor.getUsername()) && 
            doctorRepository.existsByUsername(updatedDoctor.getUsername())) {
            throw new IllegalArgumentException("Doctor with this username already exists");
        }
    }
}