package com.elearning.controller;

import com.elearning.entity.Course;
import com.elearning.entity.Lesson;
import com.elearning.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    // POST /courses - Create course
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Map<String, String> request) {
        try {
            String title = request.get("title");
            String description = request.get("description");
            
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Title is required");
            }
            
            Course course = courseService.createCourse(title, description);
            return ResponseEntity.status(HttpStatus.CREATED).body(course);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating course");
        }
    }
    
    // GET /courses - List all courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses(@RequestParam(required = false) String search) {
        try {
            List<Course> courses;
            if (search != null && !search.trim().isEmpty()) {
                courses = courseService.searchCourses(search);
            } else {
                courses = courseService.getAllCourses();
            }
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // GET /courses/{id} - Get course details
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return ResponseEntity.ok(course);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving course");
        }
    }
    
    // PUT /courses/{id} - Update course
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String title = request.get("title");
            String description = request.get("description");
            
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Title is required");
            }
            
            Course course = courseService.updateCourse(id, title, description);
            return ResponseEntity.ok(course);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating course");
        }
    }
    
    // DELETE /courses/{id} - Delete course
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok().body("Course deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting course");
        }
    }
    
    // POST /courses/{id}/lessons - Add lesson to course
    @PostMapping("/{id}/lessons")
    public ResponseEntity<?> addLessonToCourse(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String title = request.get("title");
            String content = request.get("content");
            
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Lesson title is required");
            }
            
            Lesson lesson = courseService.addLessonToCourse(id, title, content);
            return ResponseEntity.status(HttpStatus.CREATED).body(lesson);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding lesson to course");
        }
    }
    
    // GET /courses/{id}/lessons - List lessons of a course
    @GetMapping("/{id}/lessons")
    public ResponseEntity<?> getCourseLessons(@PathVariable Long id) {
        try {
            List<Lesson> lessons = courseService.getCourseLessons(id);
            return ResponseEntity.ok(lessons);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving course lessons");
        }
    }
    
    // GET /courses/{id}/stats - Get course statistics
    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getCourseStats(@PathVariable Long id) {
        try {
            CourseService.CourseStats stats = courseService.getCourseStats(id);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving course statistics");
        }
    }
}