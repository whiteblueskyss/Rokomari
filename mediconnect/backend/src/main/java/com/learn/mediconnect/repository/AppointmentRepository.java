package com.learn.mediconnect.repository;

import com.learn.mediconnect.entity.Appointment;
import com.learn.mediconnect.entity.Appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllAppointments();

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    List<Appointment> findByPatientIdAndStatus(Long patientId, AppointmentStatus status);

    // Find appointments for a specific doctor on a specific date
    List<Appointment> findByDoctorIdAndVisitingDate(Long doctorId, LocalDate visitingDate);

    // Find upcoming appointments for a doctor
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.visitingDate >= :currentDate ORDER BY a.visitingDate ASC")
    List<Appointment> findUpcomingAppointmentsByDoctor(@Param("doctorId") Long doctorId, 
                                                      @Param("currentDate") LocalDate currentDate);

    // Find upcoming appointments for a patient
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.visitingDate >= :currentDate ORDER BY a.visitingDate ASC")
    List<Appointment> findUpcomingAppointmentsByPatient(@Param("patientId") Long patientId, 
                                                       @Param("currentDate") LocalDate currentDate);

   
    // Find appointments by doctor and date ordered by serial number
    List<Appointment> findByDoctorIdAndVisitingDateOrderByVisitingSerialNumberAsc(Long doctorId, LocalDate visitingDate);
}