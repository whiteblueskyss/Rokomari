package com.elearning.service;

import com.elearning.entity.Student;
import com.elearning.entity.Course;
import com.elearning.repository.StudentRepository;
import com.elearning.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    // Create a new student
    public Student createStudent(String name, String email) {
        // Validate email format (basic validation)
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        
        // Check if student with same email already exists
        if (studentRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Student with email '" + email + "' already exists");
        }
        
        Student student = new Student(name, email);
        return studentRepository.save(student);
    }
    
    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAllByOrderByRegistrationDateDesc();
    }
    
    // Get student by ID
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }
    
    // Get student by email
    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found with email: " + email));
    }
    
    // Update student
    public Student updateStudent(Long id, String name, String email) {
        Student student = getStudentById(id);
        
        // Validate email format
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        
        // Check if new email conflicts with existing student (excluding current)
        Optional<Student> existingStudent = studentRepository.findByEmail(email);
        if (existingStudent.isPresent() && !existingStudent.get().getId().equals(id)) {
            throw new IllegalArgumentException("Student with email '" + email + "' already exists");
        }
        
        student.setName(name);
        student.setEmail(email);
        return studentRepository.save(student);
    }
    
    // Delete student
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }
    
    // Search students by name
    public List<Student> searchStudentsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentRepository.findByNameContainingIgnoreCase(name.trim());
    }
    
    // Get student's enrolled courses
    public List<Course> getStudentCourses(Long studentId) {
        Student student = getStudentById(studentId); // Validate student exists
        return enrollmentRepository.findCoursesByStudentId(studentId);
    }
    
    // Get student statistics
    public StudentStats getStudentStats(Long studentId) {
        Student student = getStudentById(studentId);
        Long enrollmentCount = enrollmentRepository.countEnrollmentsByStudentId(studentId);
        
        return new StudentStats(student.getName(), student.getEmail(), enrollmentCount);
    }
    
    // Get total students count
    public Long getTotalStudentsCount() {
        return studentRepository.countTotalStudents();
    }
    
    // Basic email validation
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Simple email pattern validation
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailPattern);
    }
    
    // Inner class for student statistics
    public static class StudentStats {
        private String name;
        private String email;
        private Long enrollmentCount;
        
        public StudentStats(String name, String email, Long enrollmentCount) {
            this.name = name;
            this.email = email;
            this.enrollmentCount = enrollmentCount;
        }
        
        public String getName() { return name; }
        public String getEmail() { return email; }
        public Long getEnrollmentCount() { return enrollmentCount; }
    }
}