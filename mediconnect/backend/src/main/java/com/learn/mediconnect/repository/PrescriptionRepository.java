package com.learn.mediconnect.repository;

import com.learn.mediconnect.entity.Prescription;
import com.learn.mediconnect.entity.Prescription.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    // Use inherited findAll() instead of custom findAllPrescriptions()

    List<Prescription> findAll();

    Prescription findById(long id);

    List<Prescription> findByPatientId(Long patientId);

    List<Prescription> findByDoctorId(Long doctorId);

    List<Prescription> findByPatientIdAndStatus(Long patientId, PrescriptionStatus status);

    List<Prescription> findByDoctorIdAndStatus(Long doctorId, PrescriptionStatus status);
    
    
    //Find prescriptions with follow-up dates before specified date
     // Search prescriptions by problem description
    

    // Find prescriptions by patient and doctor
    List<Prescription> findByPatientIdAndDoctorId(Long patientId, Long doctorId);

}