package com.elearning.service;

import com.elearning.entity.Lesson;
import com.elearning.entity.Course;
import com.elearning.repository.LessonRepository;
import com.elearning.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LessonService {
    
    @Autowired
    private LessonRepository lessonRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    // Get lesson by ID
    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + id));
    }
    
    // Update lesson
    public Lesson updateLesson(Long id, String title, String content, Integer order) {
        Lesson lesson = getLessonById(id);
        
        // Check if new title conflicts with existing lesson in same course (excluding current)
        Optional<Lesson> existingLesson = lessonRepository.findByTitleAndCourse(title, lesson.getCourse());
        if (existingLesson.isPresent() && !existingLesson.get().getId().equals(id)) {
            throw new IllegalArgumentException("Lesson with title '" + title + "' already exists in this course");
        }
        
        lesson.setTitle(title);
        lesson.setContent(content);
        
        // Update order if provided
        if (order != null && order > 0) {
            lesson.setOrder(order);
        }
        
        return lessonRepository.save(lesson);
    }
    
    // Delete lesson
    public void deleteLesson(Long id) {
        Lesson lesson = getLessonById(id);
        lessonRepository.delete(lesson);
    }
    
    // Get lessons by course ID
    public List<Lesson> getLessonsByCourseId(Long courseId) {
        // Validate course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        return lessonRepository.findByCourseIdOrderByOrderAsc(courseId);
    }
    
    // Search lessons in a course
    public List<Lesson> searchLessonsInCourse(Long courseId, String searchTerm) {
        // Validate course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getLessonsByCourseId(courseId);
        }
        
        return lessonRepository.searchLessonsInCourse(courseId, searchTerm.trim());
    }
    
    // Reorder lessons in a course
    public void reorderLessons(Long courseId, List<Long> lessonIds) {
        // Validate course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        // Update lesson orders
        for (int i = 0; i < lessonIds.size(); i++) {
            Long lessonId = lessonIds.get(i);
            Lesson lesson = getLessonById(lessonId);
            
            // Validate lesson belongs to the course
            if (!lesson.getCourse().getId().equals(courseId)) {
                throw new IllegalArgumentException("Lesson " + lessonId + " does not belong to course " + courseId);
            }
            
            lesson.setOrder(i + 1);
            lessonRepository.save(lesson);
        }
    }
    
    // Get lesson count for a course
    public Long getLessonCountByCourse(Long courseId) {
        // Validate course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        return lessonRepository.countLessonsByCourseId(courseId);
    }
    
    // Get next lesson in a course
    public Optional<Lesson> getNextLesson(Long currentLessonId) {
        Lesson currentLesson = getLessonById(currentLessonId);
        Long courseId = currentLesson.getCourse().getId();
        Integer currentOrder = currentLesson.getOrder();
        
        if (currentOrder == null) {
            return Optional.empty();
        }
        
        List<Lesson> courseLessons = lessonRepository.findByCourseIdOrderByOrderAsc(courseId);
        
        // Find next lesson with higher order
        return courseLessons.stream()
                .filter(lesson -> lesson.getOrder() != null && lesson.getOrder() > currentOrder)
                .findFirst();
    }
    
    // Get previous lesson in a course
    public Optional<Lesson> getPreviousLesson(Long currentLessonId) {
        Lesson currentLesson = getLessonById(currentLessonId);
        Long courseId = currentLesson.getCourse().getId();
        Integer currentOrder = currentLesson.getOrder();
        
        if (currentOrder == null || currentOrder <= 1) {
            return Optional.empty();
        }
        
        List<Lesson> courseLessons = lessonRepository.findByCourseIdOrderByOrderAsc(courseId);
        
        // Find previous lesson with lower order
        return courseLessons.stream()
                .filter(lesson -> lesson.getOrder() != null && lesson.getOrder() < currentOrder)
                .reduce((first, second) -> second); // Get the last (highest order) matching lesson
    }
}