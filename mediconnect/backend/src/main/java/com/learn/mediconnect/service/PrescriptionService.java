package com.learn.mediconnect.service;

import com.learn.mediconnect.entity.Prescription;
import com.learn.mediconnect.entity.Prescription.PrescriptionStatus;
import com.learn.mediconnect.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                             DoctorService doctorService,
                             PatientService patientService) {
        this.prescriptionRepository = prescriptionRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }



    public Prescription createPrescription(Prescription prescription) {
        validatePrescriptionForCreation(prescription);
        
        // Set prescription date if not provided
        if (prescription.getPrescriptionDate() == null) {
            prescription.setPrescriptionDate(LocalDate.now());
        }
        
        prescription.setStatus(PrescriptionStatus.ACTIVE);
        return prescriptionRepository.save(prescription);
    }



    public Prescription updatePrescription(Long id, Prescription updatedPrescription) {
        Prescription existingPrescription = getPrescriptionById(id);
        validatePrescriptionForUpdate(existingPrescription, updatedPrescription);
        
        // Update fields (cannot change doctor, patient, or prescription date)
        existingPrescription.setProblem(updatedPrescription.getProblem());
        existingPrescription.setTests(updatedPrescription.getTests());
        existingPrescription.setTablets(updatedPrescription.getTablets());
        existingPrescription.setCapsules(updatedPrescription.getCapsules());
        existingPrescription.setVaccines(updatedPrescription.getVaccines());
        existingPrescription.setAdvice(updatedPrescription.getAdvice());
        existingPrescription.setOther(updatedPrescription.getOther());
        existingPrescription.setFollowUpDate(updatedPrescription.getFollowUpDate());
        existingPrescription.setStatus(updatedPrescription.getStatus());
        
        return prescriptionRepository.save(existingPrescription);
    }


    @Transactional(readOnly = true)
    public Prescription getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found with ID: " + id));
        initializePrescriptionRelationships(prescription);
        return prescription;
    }



    @Transactional(readOnly = true)
    public List<Prescription> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        return initializePrescriptionRelationships(prescriptions);
    }


    public void deletePrescription(Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new IllegalArgumentException("Prescription not found with ID: " + id);
        }
        prescriptionRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
        // Verify patient exists
        patientService.getPatientById(patientId);
        List<Prescription> prescriptions = prescriptionRepository.findByPatientId(patientId);
        return initializePrescriptionRelationships(prescriptions);
    }



    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionsByDoctorId(Long doctorId) {
        // Verify doctor exists
        doctorService.getDoctorById(doctorId);
        List<Prescription> prescriptions = prescriptionRepository.findByDoctorId(doctorId);
        return initializePrescriptionRelationships(prescriptions);
    }



    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionsByPatientAndDoctor(Long patientId, Long doctorId) {
        // Verify patient and doctor exist
        patientService.getPatientById(patientId);
        doctorService.getDoctorById(doctorId);
        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdAndDoctorId(patientId, doctorId);
        return initializePrescriptionRelationships(prescriptions);
    }



    private void validatePrescriptionForCreation(Prescription prescription) {
        if (prescription == null) {
            throw new IllegalArgumentException("Prescription cannot be null");
        }
        
        if (prescription.getPatient() == null || prescription.getPatient().getId() == null) {
            throw new IllegalArgumentException("Patient is required");
        }
        
        if (prescription.getDoctor() == null || prescription.getDoctor().getId() == null) {
            throw new IllegalArgumentException("Doctor is required");
        }
        
        if (prescription.getProblem() == null || prescription.getProblem().trim().isEmpty()) {
            throw new IllegalArgumentException("Problem description is required");
        }
        
        // Verify patient and doctor exist
        patientService.getPatientById(prescription.getPatient().getId());
        doctorService.getDoctorById(prescription.getDoctor().getId());
        
        // Validate prescription date if provided
        if (prescription.getPrescriptionDate() != null && 
            prescription.getPrescriptionDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Prescription date cannot be in the future");
        }
        
        // Validate follow-up date if provided
        if (prescription.getFollowUpDate() != null && 
            prescription.getFollowUpDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Follow-up date must be today or in the future");
        }
    }



    private void validatePrescriptionForUpdate(Prescription existingPrescription, Prescription updatedPrescription) {
        if (updatedPrescription == null) {
            throw new IllegalArgumentException("Updated prescription data cannot be null");
        }
        
        // Cannot update completed or cancelled prescriptions
        if (existingPrescription.getStatus() == PrescriptionStatus.COMPLETED) {
            throw new IllegalArgumentException("Cannot update a completed prescription");
        }
        
        if (existingPrescription.getStatus() == PrescriptionStatus.CANCELED) {
            throw new IllegalArgumentException("Cannot update a cancelled prescription");
        }
        
        // Validate problem description
        if (updatedPrescription.getProblem() == null || updatedPrescription.getProblem().trim().isEmpty()) {
            throw new IllegalArgumentException("Problem description is required");
        }
        
        // Validate follow-up date if provided
        if (updatedPrescription.getFollowUpDate() != null && 
            updatedPrescription.getFollowUpDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Follow-up date must be today or in the future");
        }
    }

    // Helper method to initialize lazy relationships
    private List<Prescription> initializePrescriptionRelationships(List<Prescription> prescriptions) {
        for (Prescription prescription : prescriptions) {
            initializePrescriptionRelationships(prescription);
        }
        return prescriptions;
    }

    // Helper method to initialize lazy relationships for a single prescription
    private void initializePrescriptionRelationships(Prescription prescription) {
        // Force initialization of lazy relationships
        prescription.getDoctor().getName(); // Initialize doctor
        prescription.getPatient().getName(); // Initialize patient
    }
}