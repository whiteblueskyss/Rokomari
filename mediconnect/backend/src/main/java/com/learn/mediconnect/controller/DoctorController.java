package com.learn.mediconnect.controller;

import com.learn.mediconnect.dto.DoctorDTO;
import com.learn.mediconnect.entity.Doctor;
import com.learn.mediconnect.service.DoctorService;
import com.learn.mediconnect.validation.CreateValidation;
import com.learn.mediconnect.validation.UpdateValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@Validated(CreateValidation.class) @RequestBody DoctorDTO doctorDTO) {
        Doctor doctor = convertToEntity(doctorDTO);
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        DoctorDTO responseDTO = convertToDTO(createdDoctor);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        DoctorDTO doctorDTO = convertToDTO(doctor);
        return ResponseEntity.ok(doctorDTO);
    }

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<DoctorDTO> doctorDTOs = doctors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctorDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @Validated(UpdateValidation.class) @RequestBody DoctorDTO doctorDTO) {
        Doctor doctor = convertToEntity(doctorDTO);
        Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);
        DoctorDTO responseDTO = convertToDTO(updatedDoctor);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorDTO>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
        List<DoctorDTO> doctorDTOs = doctors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctorDTOs);
    }

    @GetMapping("/visiting-day/{day}")
    public ResponseEntity<List<DoctorDTO>> getDoctorsByVisitingDay(@PathVariable String day) {
        List<Doctor> doctors = doctorService.getDoctorsByVisitingDay(day);
        List<DoctorDTO> doctorDTOs = doctors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctorDTOs);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = doctorService.isEmailExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam String username) {
        boolean exists = doctorService.isUsernameExists(username);
        return ResponseEntity.ok(exists);
    }

    // Helper methods for entity-DTO conversion
    private Doctor convertToEntity(DoctorDTO dto) {
        Doctor doctor = new Doctor();
        doctor.setId(dto.getId());
        doctor.setName(dto.getName());
        doctor.setEmail(dto.getEmail());
        doctor.setPhone(dto.getPhone());
        doctor.setUsername(dto.getUsername());
        doctor.setPassword(dto.getPassword());
        doctor.setSpecializations(dto.getSpecializations());
        doctor.setVisitingDays(dto.getVisitingDays());
        doctor.setPic(dto.getPic());
        return doctor;
    }

    private DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setEmail(doctor.getEmail());
        dto.setPhone(doctor.getPhone());
        dto.setUsername(doctor.getUsername());
        // Exclude password for security
        dto.setSpecializations(doctor.getSpecializations());
        dto.setVisitingDays(doctor.getVisitingDays());
        dto.setPic(doctor.getPic());
        return dto;
    }
}