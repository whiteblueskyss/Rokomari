package com.elearning.service;

import com.elearning.entity.Enrollment;
import com.elearning.entity.Student;
import com.elearning.entity.Course;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.StudentRepository;
import com.elearning.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EnrollmentService {
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    // Enroll student in a course
    public Enrollment enrollStudent(Long studentId, Long courseId) {
        // Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Validate course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        // Check if student is already enrolled
        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }
        
        Enrollment enrollment = new Enrollment(student, course);
        return enrollmentRepository.save(enrollment);
    }
    
    // Enroll student in course using email
    public Enrollment enrollStudentByEmail(String email, Long courseId) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found with email: " + email));
        
        return enrollStudent(student.getId(), courseId);
    }
    
    // Unenroll student from a course
    public void unenrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new RuntimeException("Enrollment not found for student " + studentId + " in course " + courseId));
        
        enrollmentRepository.delete(enrollment);
    }
    
    // Check if student is enrolled in course
    public boolean isStudentEnrolled(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }
    
    // Get all enrollments for a student
    public List<Enrollment> getStudentEnrollments(Long studentId) {
        // Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        return enrollmentRepository.findByStudentIdOrderByEnrollmentDateDesc(studentId);
    }
    
    // Get all enrollments for a course
    public List<Enrollment> getCourseEnrollments(Long courseId) {
        // Validate course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        return enrollmentRepository.findByCourseIdOrderByEnrollmentDateDesc(courseId);
    }
    
    // Get courses enrolled by a student
    public List<Course> getStudentCourses(Long studentId) {
        // Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        return enrollmentRepository.findCoursesByStudentId(studentId);
    }
    
    // Get enrollment statistics for a course
    public CourseEnrollmentStats getCourseEnrollmentStats(Long courseId) {
        // Validate course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        Long enrollmentCount = enrollmentRepository.countEnrollmentsByCourseId(courseId);
        
        return new CourseEnrollmentStats(course.getTitle(), enrollmentCount);
    }
    
    // Get enrollment statistics for a student
    public StudentEnrollmentStats getStudentEnrollmentStats(Long studentId) {
        // Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        Long enrollmentCount = enrollmentRepository.countEnrollmentsByStudentId(studentId);
        
        return new StudentEnrollmentStats(student.getName(), student.getEmail(), enrollmentCount);
    }
    
    // Get all enrollments
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
    
    // Inner class for course enrollment statistics
    public static class CourseEnrollmentStats {
        private String courseTitle;
        private Long enrollmentCount;
        
        public CourseEnrollmentStats(String courseTitle, Long enrollmentCount) {
            this.courseTitle = courseTitle;
            this.enrollmentCount = enrollmentCount;
        }
        
        public String getCourseTitle() { return courseTitle; }
        public Long getEnrollmentCount() { return enrollmentCount; }
    }
    
    // Inner class for student enrollment statistics
    public static class StudentEnrollmentStats {
        private String studentName;
        private String studentEmail;
        private Long enrollmentCount;
        
        public StudentEnrollmentStats(String studentName, String studentEmail, Long enrollmentCount) {
            this.studentName = studentName;
            this.studentEmail = studentEmail;
            this.enrollmentCount = enrollmentCount;
        }
        
        public String getStudentName() { return studentName; }
        public String getStudentEmail() { return studentEmail; }
        public Long getEnrollmentCount() { return enrollmentCount; }
    }
}