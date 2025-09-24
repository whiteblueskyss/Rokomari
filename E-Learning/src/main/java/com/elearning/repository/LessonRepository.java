package com.elearning.repository;

import com.elearning.entity.Lesson;
import com.elearning.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    
    // Find all lessons for a specific course, ordered by lesson order
    List<Lesson> findByCourseOrderByOrderAsc(Course course);
    
    // Find all lessons for a course by course ID
    List<Lesson> findByCourseIdOrderByOrderAsc(Long courseId);
    
    // Find lesson by title and course
    Optional<Lesson> findByTitleAndCourse(String title, Course course);
    
    // Count lessons in a course
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.course.id = :courseId")
    Long countLessonsByCourseId(@Param("courseId") Long courseId);
    
    // Find lessons containing specific text in title or content
    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId AND (l.title LIKE %:searchTerm% OR l.content LIKE %:searchTerm%)")
    List<Lesson> searchLessonsInCourse(@Param("courseId") Long courseId, @Param("searchTerm") String searchTerm);
    
    // Get max lesson order for a course
    @Query("SELECT COALESCE(MAX(l.order), 0) FROM Lesson l WHERE l.course.id = :courseId")
    Integer getMaxLessonOrderByCourseId(@Param("courseId") Long courseId);
}