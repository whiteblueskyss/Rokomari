package com.elearning.repository;

import com.elearning.entity.Progress;
import com.elearning.entity.Student;
import com.elearning.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    
    // Find progress by student and lesson
    Optional<Progress> findByStudentAndLesson(Student student, Lesson lesson);
    
    // Find all progress for a student
    List<Progress> findByStudentOrderByCompletedDateDesc(Student student);
    
    // Find all progress for a student by student ID
    List<Progress> findByStudentIdOrderByCompletedDateDesc(Long studentId);
    
    // Check if student has completed a lesson
    boolean existsByStudentAndLesson(Student student, Lesson lesson);
    
    // Check if student has completed a lesson by IDs
    boolean existsByStudentIdAndLessonId(Long studentId, Long lessonId);
    
    // Count completed lessons for a student
    @Query("SELECT COUNT(p) FROM Progress p WHERE p.student.id = :studentId")
    Long countCompletedLessonsByStudentId(@Param("studentId") Long studentId);
    
    // Count completed lessons for a student in a specific course
    @Query("SELECT COUNT(p) FROM Progress p WHERE p.student.id = :studentId AND p.lesson.course.id = :courseId")
    Long countCompletedLessonsByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    
    // Get completed lessons for a student in a course
    @Query("SELECT p.lesson FROM Progress p WHERE p.student.id = :studentId AND p.lesson.course.id = :courseId ORDER BY p.lesson.order")
    List<Lesson> findCompletedLessonsByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    
    // Get all progress for a student in a specific course
    @Query("SELECT p FROM Progress p WHERE p.student.id = :studentId AND p.lesson.course.id = :courseId ORDER BY p.completedDate DESC")
    List<Progress> findProgressByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    
    // Calculate progress percentage for a student in a course
    @Query("SELECT (COUNT(p) * 100.0 / (SELECT COUNT(l) FROM Lesson l WHERE l.course.id = :courseId)) " +
           "FROM Progress p WHERE p.student.id = :studentId AND p.lesson.course.id = :courseId")
    Double calculateProgressPercentage(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}