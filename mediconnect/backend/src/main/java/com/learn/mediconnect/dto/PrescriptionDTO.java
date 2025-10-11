package com.learn.mediconnect.dto;

import com.learn.mediconnect.validation.CreateValidation;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class PrescriptionDTO {
    private Long id;
    private LocalDate prescriptionDate; // Set automatically
    
    @NotNull(message = "Patient ID is required", groups = CreateValidation.class)
    private Long patientId;
    private String patientName; // For read operations
    
    @NotNull(message = "Doctor ID is required", groups = CreateValidation.class)
    private Long doctorId;
    private String doctorName; // For read operations
    
    @NotBlank(message = "Problem description is required")
    private String problem;
    
    private List<String> tests;
    private List<String> tablets;
    private List<String> capsules;
    private List<String> vaccines;
    private String advice;
    private String other;
    private LocalDate followUpDate;
    private String status; // ACTIVE, COMPLETED, EXPIRED, CANCELED

    // Constructors
    public PrescriptionDTO() {}

    public PrescriptionDTO(Long patientId, Long doctorId, String problem) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.problem = problem;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(LocalDate prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public List<String> getTests() {
        return tests;
    }

    public void setTests(List<String> tests) {
        this.tests = tests;
    }

    public List<String> getTablets() {
        return tablets;
    }

    public void setTablets(List<String> tablets) {
        this.tablets = tablets;
    }

    public List<String> getCapsules() {
        return capsules;
    }

    public void setCapsules(List<String> capsules) {
        this.capsules = capsules;
    }

    public List<String> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<String> vaccines) {
        this.vaccines = vaccines;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public LocalDate getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}