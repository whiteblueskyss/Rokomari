package com.learn.mediconnect.controller;

import com.learn.mediconnect.dto.AppointmentDTO;
import com.learn.mediconnect.entity.Appointment;
import com.learn.mediconnect.entity.Appointment.AppointmentStatus;
import com.learn.mediconnect.entity.Doctor;
import com.learn.mediconnect.entity.Patient;
import com.learn.mediconnect.service.AppointmentService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, 
                               DoctorService doctorService, 
                               PatientService patientService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@Validated(CreateValidation.class) @RequestBody AppointmentDTO appointmentDTO) {
        Appointment appointment = convertToEntity(appointmentDTO);
        Appointment createdAppointment = appointmentService.createAppointment(appointment);
        AppointmentDTO responseDTO = convertToDTO(createdAppointment);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        AppointmentDTO appointmentDTO = convertToDTO(appointment);
        return ResponseEntity.ok(appointmentDTO);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @Validated(UpdateValidation.class) @RequestBody AppointmentDTO appointmentDTO) {
        Appointment appointment = convertToEntity(appointmentDTO);
        Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);
        AppointmentDTO responseDTO = convertToDTO(updatedAppointment);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable Long id) {
        Appointment cancelledAppointment = appointmentService.cancelAppointment(id);
        AppointmentDTO responseDTO = convertToDTO(cancelledAppointment);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<AppointmentDTO> completeAppointment(@PathVariable Long id) {
        Appointment completedAppointment = appointmentService.completeAppointment(id);
        AppointmentDTO responseDTO = convertToDTO(completedAppointment);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentDTOs);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatient(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentDTOs);
    }

    @GetMapping("/doctor/{doctorId}/upcoming")
    public ResponseEntity<List<AppointmentDTO>> getUpcomingAppointmentsByDoctor(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getUpcomingAppointmentsByDoctor(doctorId);
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentDTOs);
    }

    @GetMapping("/patient/{patientId}/upcoming")
    public ResponseEntity<List<AppointmentDTO>> getUpcomingAppointmentsByPatient(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getUpcomingAppointmentsByPatient(patientId);
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentDTOs);
    }

    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctorAndDate(
            @PathVariable Long doctorId, 
            @PathVariable String date) {
        LocalDate visitingDate = LocalDate.parse(date);
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorAndDateOrderedBySerial(doctorId, visitingDate);
        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointmentDTOs);
    }

    // Helper methods for entity-DTO conversion
    private Appointment convertToEntity(AppointmentDTO dto) {
        Appointment appointment = new Appointment();
        appointment.setId(dto.getId());
        
        // Set doctor
        if (dto.getDoctorId() != null) {
            Doctor doctor = doctorService.getDoctorById(dto.getDoctorId());
            appointment.setDoctor(doctor);
        }
        
        // Set patient
        if (dto.getPatientId() != null) {
            Patient patient = patientService.getPatientById(dto.getPatientId());
            appointment.setPatient(patient);
        }
        
        appointment.setBookingDate(dto.getBookingDate());
        appointment.setVisitingDate(dto.getVisitingDate());
        appointment.setVisitingSerialNumber(dto.getVisitingSerialNumber());
        
        // Convert status string to enum
        if (dto.getStatus() != null) {
            appointment.setStatus(AppointmentStatus.valueOf(dto.getStatus().toUpperCase()));
        }
        
        appointment.setProblemDescription(dto.getProblemDescription());
        return appointment;
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setDoctorName(appointment.getDoctor().getName());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setPatientName(appointment.getPatient().getName());
        dto.setBookingDate(appointment.getBookingDate());
        dto.setVisitingDate(appointment.getVisitingDate());
        dto.setVisitingSerialNumber(appointment.getVisitingSerialNumber());
        dto.setStatus(appointment.getStatus().name());
        dto.setProblemDescription(appointment.getProblemDescription());
        return dto;
    }
}