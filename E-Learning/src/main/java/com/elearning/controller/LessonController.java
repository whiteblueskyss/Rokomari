package com.elearning.controller;

import com.elearning.entity.Lesson;
import com.elearning.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lessons")
public class LessonController {
    
    @Autowired
    private LessonService lessonService;
    
    // GET /lessons - List all lessons (with optional course filter)
    @GetMapping
    public ResponseEntity<List<Lesson>> getAllLessons(@RequestParam(required = false) Long courseId,
                                                     @RequestParam(required = false) String search) {
        try {
            List<Lesson> lessons;
            if (courseId != null) {
                lessons = lessonService.getLessonsByCourseId(courseId);
            } else {
                // Since there's no getAllLessons method, we'll return an error for now
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // GET /lessons/{id} - Get lesson details
    @GetMapping("/{id}")
    public ResponseEntity<?> getLessonById(@PathVariable Long id) {
        try {
            Lesson lesson = lessonService.getLessonById(id);
            return ResponseEntity.ok(lesson);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving lesson");
        }
    }
    
    // PUT /lessons/{id} - Update lesson
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String title = request.get("title");
            String content = request.get("content");
            
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Title is required");
            }
            
            Lesson lesson = lessonService.updateLesson(id, title, content, null); // No order parameter
            return ResponseEntity.ok(lesson);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating lesson");
        }
    }
    
    // DELETE /lessons/{id} - Delete lesson
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id) {
        try {
            lessonService.deleteLesson(id);
            return ResponseEntity.ok().body("Lesson deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting lesson");
        }
    }
    
    // GET /lessons/{id}/stats - Get lesson statistics (not supported by current service)
    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getLessonStats(@PathVariable Long id) {
        return ResponseEntity.badRequest().body("Lesson statistics not supported by current service");
    }
}