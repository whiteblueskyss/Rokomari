package com.learn.mediconnect.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class AppointmentDTO {
    private Long id;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    private String doctorName; 
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    private String patientName;
    
    private LocalDate bookingDate; // Set automatically
    
    @NotNull(message = "Visiting date is required")
    private LocalDate visitingDate;
    
    private Integer visitingSerialNumber; // Set automatically
    private String status; // SCHEDULED, COMPLETED, CANCELED
    private String problemDescription;

    // Constructors
    public AppointmentDTO() {}

    public AppointmentDTO(Long doctorId, Long patientId, LocalDate visitingDate) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.visitingDate = visitingDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getVisitingDate() {
        return visitingDate;
    }

    public void setVisitingDate(LocalDate visitingDate) {
        this.visitingDate = visitingDate;
    }

    public Integer getVisitingSerialNumber() {
        return visitingSerialNumber;
    }

    public void setVisitingSerialNumber(Integer visitingSerialNumber) {
        this.visitingSerialNumber = visitingSerialNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }
}