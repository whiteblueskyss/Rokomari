package com.elearning.controller;

import com.elearning.entity.Enrollment;
import com.elearning.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    // POST /enrollments - Enroll student in course
    @PostMapping
    public ResponseEntity<?> enrollStudent(@RequestBody Map<String, String> request) {
        try {
            String studentIdStr = request.get("studentId");
            String courseIdStr = request.get("courseId");
            
            if (studentIdStr == null || courseIdStr == null) {
                return ResponseEntity.badRequest().body("studentId and courseId are required");
            }
            
            Long studentId = Long.parseLong(studentIdStr);
            Long courseId = Long.parseLong(courseIdStr);
            
            Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseId);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid student ID or course ID format");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating enrollment");
        }
    }
    
    // GET /enrollments - List all enrollments (with optional filters)
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments(@RequestParam(required = false) Long studentId,
                                                             @RequestParam(required = false) Long courseId) {
        try {
            List<Enrollment> enrollments;
            if (studentId != null) {
                // Student filter
                enrollments = enrollmentService.getStudentEnrollments(studentId);
            } else if (courseId != null) {
                // Course filter
                enrollments = enrollmentService.getCourseEnrollments(courseId);
            } else {
                // No filters - all enrollments
                enrollments = enrollmentService.getAllEnrollments();
            }
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // GET /enrollments/{id} - Get enrollment details (not supported by current service)
    @GetMapping("/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.badRequest().body("Individual enrollment retrieval not supported");
    }
    
    // DELETE /enrollments/{id} - Unenroll student from course (using different method signature)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> unenrollStudent(@PathVariable Long id,
                                            @RequestParam Long studentId,
                                            @RequestParam Long courseId) {
        try {
            enrollmentService.unenrollStudent(studentId, courseId);
            return ResponseEntity.ok().body("Student unenrolled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing enrollment");
        }
    }
    
    // PUT /enrollments/{id}/completion - Mark enrollment as completed (not supported)
    @PutMapping("/{id}/completion")
    public ResponseEntity<?> markEnrollmentCompleted(@PathVariable Long id) {
        return ResponseEntity.badRequest().body("Enrollment completion not supported");
    }
    
    // GET /enrollments/{id}/progress - Get enrollment progress (not supported)
    @GetMapping("/{id}/progress")
    public ResponseEntity<?> getEnrollmentProgress(@PathVariable Long id) {
        return ResponseEntity.badRequest().body("Enrollment progress not supported");
    }
}