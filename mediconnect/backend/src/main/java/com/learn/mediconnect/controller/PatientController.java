package com.learn.mediconnect.controller;

import com.learn.mediconnect.dto.PatientDTO;
import com.learn.mediconnect.entity.Patient;
import com.learn.mediconnect.service.PatientService;
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
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Validated(CreateValidation.class) @RequestBody PatientDTO patientDTO) {
        Patient patient = convertToEntity(patientDTO);
        Patient createdPatient = patientService.createPatient(patient);
        PatientDTO responseDTO = convertToDTO(createdPatient);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        PatientDTO patientDTO = convertToDTO(patient);
        return ResponseEntity.ok(patientDTO);
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        List<PatientDTO> patientDTOs = patients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patientDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Validated(UpdateValidation.class) @RequestBody PatientDTO patientDTO) {
        Patient patient = convertToEntity(patientDTO);
        Patient updatedPatient = patientService.updatePatient(id, patient);
        PatientDTO responseDTO = convertToDTO(updatedPatient);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = patientService.isEmailExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam String username) {
        boolean exists = patientService.isUsernameExists(username);
        return ResponseEntity.ok(exists);
    }

    // Helper methods for entity-DTO conversion
    private Patient convertToEntity(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setUsername(dto.getUsername());
        patient.setPassword(dto.getPassword());
        patient.setPic(dto.getPic());
        patient.setAge(dto.getAge());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        return patient;
    }

    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setPhone(patient.getPhone());
        dto.setUsername(patient.getUsername());
        // Exclude password for security
        dto.setPic(patient.getPic());
        dto.setAge(patient.getAge());
        dto.setGender(patient.getGender());
        dto.setAddress(patient.getAddress());
        return dto;
    }
}