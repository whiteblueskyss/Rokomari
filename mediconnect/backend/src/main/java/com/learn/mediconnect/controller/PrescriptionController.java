package com.learn.mediconnect.controller;

import com.learn.mediconnect.dto.PrescriptionDTO;
import com.learn.mediconnect.entity.Prescription;
import com.learn.mediconnect.entity.Prescription.PrescriptionStatus;
import com.learn.mediconnect.entity.Doctor;
import com.learn.mediconnect.entity.Patient;
import com.learn.mediconnect.service.PrescriptionService;
import com.learn.mediconnect.service.DoctorService;
import com.learn.mediconnect.service.PatientService;
import com.learn.mediconnect.validation.CreateValidation;
import com.learn.mediconnect.validation.UpdateValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService, 
                                DoctorService doctorService, 
                                PatientService patientService) {
        this.prescriptionService = prescriptionService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PrescriptionDTO> createPrescription(@Validated(CreateValidation.class) @RequestBody PrescriptionDTO prescriptionDTO) {
        Prescription prescription = convertToEntity(prescriptionDTO);
        Prescription createdPrescription = prescriptionService.createPrescription(prescription);
        PrescriptionDTO responseDTO = convertToDTO(createdPrescription);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable Long id) {
        Prescription prescription = prescriptionService.getPrescriptionById(id);
        PrescriptionDTO prescriptionDTO = convertToDTO(prescription);
        return ResponseEntity.ok(prescriptionDTO);
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionDTO>> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        List<PrescriptionDTO> prescriptionDTOs = prescriptions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(prescriptionDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> updatePrescription(@PathVariable Long id, @Validated(UpdateValidation.class) @RequestBody PrescriptionDTO prescriptionDTO) {
        Prescription prescription = convertToEntity(prescriptionDTO);
        Prescription updatedPrescription = prescriptionService.updatePrescription(id, prescription);
        PrescriptionDTO responseDTO = convertToDTO(updatedPrescription);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByPatient(@PathVariable Long patientId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientId(patientId);
        List<PrescriptionDTO> prescriptionDTOs = prescriptions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(prescriptionDTOs);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByDoctor(@PathVariable Long doctorId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctorId(doctorId);
        List<PrescriptionDTO> prescriptionDTOs = prescriptions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(prescriptionDTOs);
    }

    @GetMapping("/patient/{patientId}/doctor/{doctorId}")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByPatientAndDoctor(
            @PathVariable Long patientId, 
            @PathVariable Long doctorId) {
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientAndDoctor(patientId, doctorId);
        List<PrescriptionDTO> prescriptionDTOs = prescriptions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(prescriptionDTOs);
    }

    // Helper methods for entity-DTO conversion
    private Prescription convertToEntity(PrescriptionDTO dto) {
        Prescription prescription = new Prescription();
        prescription.setId(dto.getId());
        prescription.setPrescriptionDate(dto.getPrescriptionDate());
        
        // Set patient
        if (dto.getPatientId() != null) {
            Patient patient = patientService.getPatientById(dto.getPatientId());
            prescription.setPatient(patient);
        }
        
        // Set doctor
        if (dto.getDoctorId() != null) {
            Doctor doctor = doctorService.getDoctorById(dto.getDoctorId());
            prescription.setDoctor(doctor);
        }
        
        prescription.setProblem(dto.getProblem());
        prescription.setTests(dto.getTests());
        prescription.setTablets(dto.getTablets());
        prescription.setCapsules(dto.getCapsules());
        prescription.setVaccines(dto.getVaccines());
        prescription.setAdvice(dto.getAdvice());
        prescription.setOther(dto.getOther());
        prescription.setFollowUpDate(dto.getFollowUpDate());
        
        // Convert status string to enum
        if (dto.getStatus() != null) {
            prescription.setStatus(PrescriptionStatus.valueOf(dto.getStatus().toUpperCase()));
        }
        
        return prescription;
    }

    private PrescriptionDTO convertToDTO(Prescription prescription) {
        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setId(prescription.getId());
        dto.setPrescriptionDate(prescription.getPrescriptionDate());
        dto.setPatientId(prescription.getPatient().getId());
        dto.setPatientName(prescription.getPatient().getName());
        dto.setDoctorId(prescription.getDoctor().getId());
        dto.setDoctorName(prescription.getDoctor().getName());
        dto.setProblem(prescription.getProblem());
        dto.setTests(prescription.getTests());
        dto.setTablets(prescription.getTablets());
        dto.setCapsules(prescription.getCapsules());
        dto.setVaccines(prescription.getVaccines());
        dto.setAdvice(prescription.getAdvice());
        dto.setOther(prescription.getOther());
        dto.setFollowUpDate(prescription.getFollowUpDate());
        dto.setStatus(prescription.getStatus().name());
        return dto;
    }
}