package com.learn.mediconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


import java.time.LocalDate;


@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Doctor is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "Booking date is required")
    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @NotNull(message = "Visiting date is required")
    @Column(name = "visiting_date", nullable = false)
    private LocalDate visitingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(name = "problem_description", columnDefinition = "TEXT")
    private String problemDescription;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    // Appointment Status Enum
    public enum AppointmentStatus {
        SCHEDULED("scheduled"),
        COMPLETED("completed"),
        CANCELED("canceled");

        private final String value;

        AppointmentStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // Constructors
    public Appointment() {}

    public Appointment(Doctor doctor, Patient patient, LocalDate bookingDate, LocalDate visitingDate) {
        this.doctor = doctor;
        this.patient = patient;
        this.bookingDate = bookingDate;
        this.visitingDate = visitingDate;
        this.status = AppointmentStatus.SCHEDULED;
    }

    public Appointment(Doctor doctor, Patient patient, LocalDate bookingDate, LocalDate visitingDate, String problemDescription) {
        this.doctor = doctor;
        this.patient = patient;
        this.bookingDate = bookingDate;
        this.visitingDate = visitingDate;
        this.problemDescription = problemDescription;
        this.status = AppointmentStatus.SCHEDULED;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
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

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }



    // Utility methods
    public boolean isScheduled() {
        return this.status == AppointmentStatus.SCHEDULED;
    }

    public boolean isCompleted() {
        return this.status == AppointmentStatus.COMPLETED;
    }

    public boolean isCanceled() {
        return this.status == AppointmentStatus.CANCELED;
    }



    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", doctorId=" + (doctor != null ? doctor.getId() : null) +
                ", patientId=" + (patient != null ? patient.getId() : null) +
                ", bookingDate=" + bookingDate +
                ", visitingDate=" + visitingDate +
                ", status=" + status +
                ", problemDescription='" + problemDescription + '\'' +
                '}';
    }
}