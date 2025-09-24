package com.elearning.controller;

import com.elearning.entity.Progress;
import com.elearning.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/progress")
public class ProgressController {
    
    @Autowired
    private ProgressService progressService;
    
    // POST /progress - Mark lesson as completed
    @PostMapping
    public ResponseEntity<?> markLessonCompleted(@RequestBody Map<String, String> request) {
        try {
            String studentIdStr = request.get("studentId");
            String lessonIdStr = request.get("lessonId");
            
            if (studentIdStr == null || lessonIdStr == null) {
                return ResponseEntity.badRequest().body("studentId and lessonId are required");
            }
            
            Long studentId = Long.parseLong(studentIdStr);
            Long lessonId = Long.parseLong(lessonIdStr);
            
            Progress progress = progressService.markLessonComplete(studentId, lessonId);
            return ResponseEntity.status(HttpStatus.CREATED).body(progress);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid student ID or lesson ID format");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error marking lesson as completed");
        }
    }
    
    // GET /progress - List all progress records (with optional filters)
    @GetMapping
    public ResponseEntity<List<Progress>> getAllProgress(@RequestParam(required = false) Long studentId,
                                                        @RequestParam(required = false) Long lessonId,
                                                        @RequestParam(required = false) Long courseId) {
        try {
            List<Progress> progressList;
            if (studentId != null && courseId != null) {
                // Student and course filters
                progressList = progressService.getStudentProgressInCourse(studentId, courseId);
            } else if (studentId != null) {
                // Student filter only
                progressList = progressService.getStudentProgress(studentId);
            } else {
                // No specific filters supported by current service, return student progress if studentId provided
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(progressList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // GET /progress/{id} - Get progress details (not supported by current service)
    @GetMapping("/{id}")
    public ResponseEntity<?> getProgressById(@PathVariable Long id) {
        return ResponseEntity.badRequest().body("Individual progress record retrieval not supported");
    }
    
    // DELETE /progress/{id} - Remove progress record (not supported by current service)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeProgress(@PathVariable Long id) {
        return ResponseEntity.badRequest().body("Progress removal not supported");
    }
    
    // GET /progress/stats/student/{studentId} - Get student progress statistics
    @GetMapping("/stats/student/{studentId}")
    public ResponseEntity<?> getStudentProgressStats(@PathVariable Long studentId) {
        try {
            ProgressService.StudentOverallProgress stats = progressService.getStudentOverallProgress(studentId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving student progress statistics");
        }
    }
    
    // GET /progress/stats/course/{courseId} - Get course progress percentage for student
    @GetMapping("/stats/course/{courseId}")
    public ResponseEntity<?> getCourseProgressStats(@PathVariable Long courseId,
                                                   @RequestParam Long studentId) {
        try {
            ProgressService.StudentCourseProgress stats = progressService.getStudentCourseProgress(studentId, courseId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving course progress statistics");
        }
    }
}