package com.elearning.repository;

import com.elearning.entity.Enrollment;
import com.elearning.entity.Student;
import com.elearning.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    // Find enrollment by student and course
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
    
    // Find all enrollments for a student
    List<Enrollment> findByStudentOrderByEnrollmentDateDesc(Student student);
    
    // Find all enrollments for a student by student ID
    List<Enrollment> findByStudentIdOrderByEnrollmentDateDesc(Long studentId);
    
    // Find all enrollments for a course
    List<Enrollment> findByCourseOrderByEnrollmentDateDesc(Course course);
    
    // Find all enrollments for a course by course ID
    List<Enrollment> findByCourseIdOrderByEnrollmentDateDesc(Long courseId);
    
    // Check if student is enrolled in course
    boolean existsByStudentAndCourse(Student student, Course course);
    
    // Check if student is enrolled in course by IDs
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    
    // Count enrollments for a course
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId")
    Long countEnrollmentsByCourseId(@Param("courseId") Long courseId);
    
    // Count total enrollments for a student
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId")
    Long countEnrollmentsByStudentId(@Param("studentId") Long studentId);
    
    // Get courses enrolled by a student
    @Query("SELECT e.course FROM Enrollment e WHERE e.student.id = :studentId ORDER BY e.enrollmentDate DESC")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);
}