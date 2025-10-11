package com.learn.mediconnect.dto;

import jakarta.validation.constraints.*;

public class DoctorDTO {
    private Long id;
    
    @NotBlank(message = "Doctor name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @Pattern(regexp = "^[+]?[0-9\\-\\s]+$", message = "Phone number format invalid")
    private String phone;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    // Password is optional - only included for create/update operations
    private String password;
    
    @NotBlank(message = "Specialization is required")
    private String specializations;
    
    private String visitingDays;
    private String pic;

    // Constructors
    public DoctorDTO() {}

    public DoctorDTO(Long id, String name, String email, String username, String specializations) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
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
}