package com.learn.mediconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Prescription Entity - Represents prescriptions issued by doctors to patients
 * Maps to the 'prescriptions' table in PostgreSQL database
 */
@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Date of writing prescription is required")
    @Column(name = "prescription_date", nullable = false)
    private LocalDateTime prescriptionDate;

    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "Doctor is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotBlank(message = "Problem description is required")
    @Column(name = "problem", columnDefinition = "TEXT", nullable = false)
    private String problem;

    @Column(name = "tests", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> tests;

    @Column(name = "tablets", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> tablets;

    @Column(name = "capsules", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> capsules;

    @Column(name = "vaccines", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> vaccines;

    @Column(name = "advice", columnDefinition = "TEXT")
    private String advice;

    @Column(name = "other", columnDefinition = "TEXT")
    private String other;

    @Column(name = "follow_up_date")
    private LocalDateTime followUpDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PrescriptionStatus status = PrescriptionStatus.ACTIVE;



    @OneToOne(mappedBy = "prescription", fetch = FetchType.LAZY)
    private Appointment appointment;

    // Prescription Status Enum
    public enum PrescriptionStatus {
        ACTIVE("active"),
        COMPLETED("completed"),
        EXPIRED("expired"),
        CANCELED("canceled");

        private final String value;

        PrescriptionStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // Constructors
    public Prescription() {}

    public Prescription(Patient patient, Doctor doctor, String problem) {
        this.patient = patient;
        this.doctor = doctor;
        this.problem = problem;
        this.prescriptionDate = LocalDateTime.now();
        this.status = PrescriptionStatus.ACTIVE;
    }

    public Prescription(Patient patient, Doctor doctor, String problem, LocalDateTime prescriptionDate) {
        this.patient = patient;
        this.doctor = doctor;
        this.problem = problem;
        this.prescriptionDate = prescriptionDate;
        this.status = PrescriptionStatus.ACTIVE;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(LocalDateTime prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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

    public LocalDateTime getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(LocalDateTime followUpDate) {
        this.followUpDate = followUpDate;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }



    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    // Utility methods
    public boolean isActive() {
        return this.status == PrescriptionStatus.ACTIVE;
    }

    public boolean isCompleted() {
        return this.status == PrescriptionStatus.COMPLETED;
    }

    public boolean isExpired() {
        return this.status == PrescriptionStatus.EXPIRED;
    }

    public boolean isCanceled() {
        return this.status == PrescriptionStatus.CANCELED;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", prescriptionDate=" + prescriptionDate +
                ", patientId=" + (patient != null ? patient.getId() : null) +
                ", doctorId=" + (doctor != null ? doctor.getId() : null) +
                ", problem='" + problem + '\'' +
                ", status=" + status +
                ", testsCount=" + (tests != null ? tests.size() : 0) +
                ", tabletsCount=" + (tablets != null ? tablets.size() : 0) +
                ", capsulesCount=" + (capsules != null ? capsules.size() : 0) +
                ", vaccinesCount=" + (vaccines != null ? vaccines.size() : 0) +
                '}';
    }
}