package com.elearning.repository;

import com.elearning.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Find student by email
    Optional<Student> findByEmail(String email);
    
    // Find students by name (case-insensitive)
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> findByNameContainingIgnoreCase(@Param("name") String name);
    
    // Get all students ordered by registration date (newest first)
    List<Student> findAllByOrderByRegistrationDateDesc();
    
    // Check if student exists by email
    boolean existsByEmail(String email);
    
    // Count total students
    @Query("SELECT COUNT(s) FROM Student s")
    Long countTotalStudents();
}