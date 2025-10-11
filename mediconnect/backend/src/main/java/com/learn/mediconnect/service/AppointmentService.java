package com.learn.mediconnect.service;

import com.learn.mediconnect.entity.Appointment;
import com.learn.mediconnect.entity.Appointment.AppointmentStatus;
import com.learn.mediconnect.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, 
                            DoctorService doctorService, 
                            PatientService patientService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public Appointment createAppointment(Appointment appointment) {
        validateAppointmentForCreation(appointment);
        appointment.setBookingDate(LocalDate.now());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        
        // Generate and set the next serial number for this doctor and date
        Integer nextSerialNumber = getNextSerialNumber(appointment.getDoctor().getId(), appointment.getVisitingDate());
        appointment.setVisitingSerialNumber(nextSerialNumber);
        
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
        Appointment existingAppointment = getAppointmentById(id);
        validateAppointmentForUpdate(existingAppointment, updatedAppointment);
        
        // Check if visiting date is being changed
        boolean dateChanged = !existingAppointment.getVisitingDate().equals(updatedAppointment.getVisitingDate());
        
        // Update fields
        existingAppointment.setVisitingDate(updatedAppointment.getVisitingDate());
        existingAppointment.setProblemDescription(updatedAppointment.getProblemDescription());
        existingAppointment.setStatus(updatedAppointment.getStatus());
        
        // If date changed, assign new serial number for the new date
        if (dateChanged) {
            Integer newSerialNumber = getNextSerialNumber(existingAppointment.getDoctor().getId(), updatedAppointment.getVisitingDate());
            existingAppointment.setVisitingSerialNumber(newSerialNumber);
        }
        
        return appointmentRepository.save(existingAppointment);
    }

    @Transactional(readOnly = true)
    public Appointment getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + id));
        initializeAppointmentRelationships(appointment);
        return appointment;
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return initializeAppointmentRelationships(appointments);
    }

    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Appointment not found with ID: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    public Appointment cancelAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalArgumentException("Cannot cancel a completed appointment");
        }
        
        if (appointment.getStatus() == AppointmentStatus.CANCELED) {
            throw new IllegalArgumentException("Appointment is already cancelled");
        }
        
        appointment.setStatus(AppointmentStatus.CANCELED);
        return appointmentRepository.save(appointment);
    }


    public Appointment completeAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        
        if (appointment.getStatus() == AppointmentStatus.CANCELED) {
            throw new IllegalArgumentException("Cannot complete a cancelled appointment");
        }
        
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalArgumentException("Appointment is already completed");
        }
        
        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointmentRepository.save(appointment);
    }

    
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        // Verify doctor exists
        doctorService.getDoctorById(doctorId);
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return initializeAppointmentRelationships(appointments);
    }

    
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        // Verify patient exists
        patientService.getPatientById(patientId);
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return initializeAppointmentRelationships(appointments);
    }

    
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDoctorAndStatus(Long doctorId, AppointmentStatus status) {
        // Verify doctor exists
        doctorService.getDoctorById(doctorId);
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndStatus(doctorId, status);
        appointments.forEach(this::initializeAppointmentRelationships);
        return appointments;
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByPatientAndStatus(Long patientId, AppointmentStatus status) {
        // Verify patient exists
        patientService.getPatientById(patientId);
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndStatus(patientId, status);
        appointments.forEach(this::initializeAppointmentRelationships);
        return appointments;
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        // Verify doctor exists
        doctorService.getDoctorById(doctorId);
        
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndVisitingDate(doctorId, date);
        return initializeAppointmentRelationships(appointments);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointmentsByDoctor(Long doctorId) {
        // Verify doctor exists
        doctorService.getDoctorById(doctorId);
        List<Appointment> appointments = appointmentRepository.findUpcomingAppointmentsByDoctor(doctorId, LocalDate.now());
        return initializeAppointmentRelationships(appointments);
    }

    
    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointmentsByPatient(Long patientId) {
        // Verify patient exists
        patientService.getPatientById(patientId);
        List<Appointment> appointments = appointmentRepository.findUpcomingAppointmentsByPatient(patientId, LocalDate.now());
        return initializeAppointmentRelationships(appointments);
    }

    
    
    @Transactional(readOnly = true)
    public Integer getNextSerialNumber(Long doctorId, LocalDate visitingDate) {
        List<Appointment> existingAppointments = appointmentRepository.findByDoctorIdAndVisitingDate(doctorId, visitingDate);
        
        if (existingAppointments.isEmpty()) {
            return 1;
        }
        
        // Find the maximum serial number
        Integer maxSerialNumber = existingAppointments.stream()
                .mapToInt(a -> a.getVisitingSerialNumber() != null ? a.getVisitingSerialNumber() : 0)
                .max()
                .orElse(0);
        
        return maxSerialNumber + 1;
    }

    
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDoctorAndDateOrderedBySerial(Long doctorId, LocalDate visitingDate) {
        // Verify doctor exists
        doctorService.getDoctorById(doctorId);
        
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndVisitingDateOrderByVisitingSerialNumberAsc(doctorId, visitingDate);
        return initializeAppointmentRelationships(appointments);
    }

    
    private void validateAppointmentForCreation(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        
        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
            throw new IllegalArgumentException("Doctor is required");
        }
        
        if (appointment.getPatient() == null || appointment.getPatient().getId() == null) {
            throw new IllegalArgumentException("Patient is required");
        }
        
        if (appointment.getVisitingDate() == null) {
            throw new IllegalArgumentException("Visiting date is required");
        }
        
        // Verify doctor and patient exist
        doctorService.getDoctorById(appointment.getDoctor().getId());
        patientService.getPatientById(appointment.getPatient().getId());
        
        // Check if visiting date is today or in the future
        if (appointment.getVisitingDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Visiting date must be today or in the future");
        }
    }


    private void validateAppointmentForUpdate(Appointment existingAppointment, Appointment updatedAppointment) {
        if (updatedAppointment == null) {
            throw new IllegalArgumentException("Updated appointment data cannot be null");
        }
        
        // Check if appointment can be updated
        if (existingAppointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalArgumentException("Cannot update a completed appointment");
        }
        
        if (existingAppointment.getStatus() == AppointmentStatus.CANCELED) {
            throw new IllegalArgumentException("Cannot update a cancelled appointment");
        }
        
        // If visiting date is being changed, validate it
        if (updatedAppointment.getVisitingDate() != null && 
            !updatedAppointment.getVisitingDate().equals(existingAppointment.getVisitingDate())) {
            
            // Check if new visiting date is today or in the future
            if (updatedAppointment.getVisitingDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("New visiting date must be today or in the future");
            }
        }
    }
    
    // Helper method to initialize lazy relationships
    private List<Appointment> initializeAppointmentRelationships(List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            initializeAppointmentRelationships(appointment);
        }
        return appointments;
    }

    // Helper method to initialize lazy relationships for a single appointment
    private void initializeAppointmentRelationships(Appointment appointment) {
        // Force initialization of lazy relationships
        appointment.getDoctor().getName(); // Initialize doctor
        appointment.getPatient().getName(); // Initialize patient
    }
}