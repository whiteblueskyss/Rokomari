package com.learn.mediconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;


@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Doctor name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    @Column(name = "phone")
    private String phone;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "Specialization is required")
    @Size(max = 255, message = "Specialization cannot exceed 255 characters")
    @Column(name = "specializations", nullable = false)
    private String specializations;

    @Size(max = 255, message = "Visiting days cannot exceed 255 characters")
    @Column(name = "visiting_days")
    private String visitingDays;

    @Size(max = 255, message = "Picture URL cannot exceed 255 characters")
    @Column(name = "pic")
    private String pic;



    // Relationships
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions;

    // Constructors
    public Doctor() {}

    public Doctor(Long id, String name, String email, String username, String password, String specializations) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.specializations = specializations;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpecializations() {
        return specializations;
    }

    public void setSpecializations(String specializations) {
        this.specializations = specializations;
    }

    public String getVisitingDays() {
        return visitingDays;
    }

    public void setVisitingDays(String visitingDays) {
        this.visitingDays = visitingDays;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }



    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    /**
     * Returns a string representation of the Doctor object for debugging and logging purposes.
     * Excludes sensitive fields like password for security reasons.
     * 
     * return formatted string with key doctor information
     */
    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", specializations='" + specializations + '\'' +
                ", visitingDays='" + visitingDays + '\'' +
                '}';
    }
}