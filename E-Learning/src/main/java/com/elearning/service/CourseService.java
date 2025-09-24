package com.elearning.service;

import com.elearning.entity.Course;
import com.elearning.entity.Lesson;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private LessonRepository lessonRepository;
    
    // Create a new course
    public Course createCourse(String title, String description) {
        // Check if course with same title already exists
        if (courseRepository.existsByTitle(title)) {
            throw new IllegalArgumentException("Course with title '" + title + "' already exists");
        }
        
        Course course = new Course(title, description);
        return courseRepository.save(course);
    }
    
    // Get all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAllByOrderByCreatedDateDesc();
    }
    
    // Get course by ID
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }
    
    // Update course
    public Course updateCourse(Long id, String title, String description) {
        Course course = getCourseById(id);
        
        // Check if new title conflicts with existing course (excluding current)
        Optional<Course> existingCourse = courseRepository.findByTitle(title);
        if (existingCourse.isPresent() && !existingCourse.get().getId().equals(id)) {
            throw new IllegalArgumentException("Course with title '" + title + "' already exists");
        }
        
        course.setTitle(title);
        course.setDescription(description);
        return courseRepository.save(course);
    }
    
    // Delete course
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }
    
    // Search courses
    public List<Course> searchCourses(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllCourses();
        }
        return courseRepository.searchCourses(searchTerm.trim());
    }
    
    // Get lessons for a course
    public List<Lesson> getCourseLessons(Long courseId) {
        Course course = getCourseById(courseId); // Validate course exists
        return lessonRepository.findByCourseIdOrderByOrderAsc(courseId);
    }
    
    // Add lesson to course
    public Lesson addLessonToCourse(Long courseId, String title, String content) {
        Course course = getCourseById(courseId);
        
        // Check if lesson with same title exists in this course
        Optional<Lesson> existingLesson = lessonRepository.findByTitleAndCourse(title, course);
        if (existingLesson.isPresent()) {
            throw new IllegalArgumentException("Lesson with title '" + title + "' already exists in this course");
        }
        
        // Get next lesson order
        Integer maxOrder = lessonRepository.getMaxLessonOrderByCourseId(courseId);
        int nextOrder = (maxOrder != null) ? maxOrder + 1 : 1;
        
        Lesson lesson = new Lesson(title, content, course);
        lesson.setOrder(nextOrder);
        return lessonRepository.save(lesson);
    }
    
    // Get course statistics
    public CourseStats getCourseStats(Long courseId) {
        Course course = getCourseById(courseId);
        Long lessonCount = lessonRepository.countLessonsByCourseId(courseId);
        
        return new CourseStats(course.getTitle(), lessonCount);
    }
    
    // Inner class for course statistics
    public static class CourseStats {
        private String title;
        private Long lessonCount;
        
        public CourseStats(String title, Long lessonCount) {
            this.title = title;
            this.lessonCount = lessonCount;
        }
        
        public String getTitle() { return title; }
        public Long getLessonCount() { return lessonCount; }
    }
}