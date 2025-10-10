package com.learn.mediconnect.repository;

import com.learn.mediconnect.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findAll();

    Doctor findById(long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
    
    // Find doctors by specialization
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.specializations) = LOWER(:specialization)")
    List<Doctor> findBySpecialization(@Param("specialization") String specialization);

    // Find doctors by visiting days
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.visitingDays) = LOWER(:day)")
    List<Doctor> findByVisitingDays(@Param("day") String day);
}