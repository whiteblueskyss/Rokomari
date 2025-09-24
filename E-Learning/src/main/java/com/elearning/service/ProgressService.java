package com.elearning.service;

import com.elearning.entity.Progress;
import com.elearning.entity.Student;
import com.elearning.entity.Lesson;
import com.elearning.repository.ProgressRepository;
import com.elearning.repository.StudentRepository;
import com.elearning.repository.LessonRepository;
import com.elearning.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProgressService {
    
    @Autowired
    private ProgressRepository progressRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private LessonRepository lessonRepository;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    // Mark lesson as complete for a student
    public Progress markLessonComplete(Long studentId, Long lessonId) {
        // Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Validate lesson exists
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));
        
        // Check if student is enrolled in the course containing this lesson
        if (!enrollmentRepository.existsByStudentIdAndCourseId(studentId, lesson.getCourse().getId())) {
            throw new IllegalArgumentException("Student is not enrolled in the course containing this lesson");
        }
        
        // Check if lesson is already completed
        if (progressRepository.existsByStudentAndLesson(student, lesson)) {
            throw new IllegalArgumentException("Lesson is already completed by this student");
        }
        
        Progress progress = new Progress(student, lesson);
        return progressRepository.save(progress);
    }
    
    // Unmark lesson completion (remove progress)
    public void unmarkLessonComplete(Long studentId, Long lessonId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));
        
        Progress progress = progressRepository.findByStudentAndLesson(student, lesson)
                .orElseThrow(() -> new RuntimeException("Progress not found for student " + studentId + " and lesson " + lessonId));
        
        progressRepository.delete(progress);
    }
    
    // Check if student has completed a lesson
    public boolean isLessonCompleted(Long studentId, Long lessonId) {
        return progressRepository.existsByStudentIdAndLessonId(studentId, lessonId);
    }
    
    // Get all progress for a student
    public List<Progress> getStudentProgress(Long studentId) {
        // Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        return progressRepository.findByStudentIdOrderByCompletedDateDesc(studentId);
    }
    
    // Get student progress for a specific course
    public List<Progress> getStudentProgressInCourse(Long studentId, Long courseId) {
        // Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Check if student is enrolled in the course
        if (!enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }
        
        return progressRepository.findProgressByStudentIdAndCourseId(studentId, courseId);
    }
    
    // Get completed lessons for a student in a course
    public List<Lesson> getCompletedLessons(Long studentId, Long courseId) {
        // Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Check if student is enrolled in the course
        if (!enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }
        
        return progressRepository.findCompletedLessonsByStudentIdAndCourseId(studentId, courseId);
    }
    
    // Calculate progress percentage for a student in a course
    public Double getProgressPercentage(Long studentId, Long courseId) {
        // Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Check if student is enrolled in the course
        if (!enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }
        
        Double percentage = progressRepository.calculateProgressPercentage(studentId, courseId);
        return percentage != null ? percentage : 0.0;
    }
    
    // Get detailed progress statistics for a student in a course
    public StudentCourseProgress getStudentCourseProgress(Long studentId, Long courseId) {
        // Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Check if student is enrolled in the course
        if (!enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }
        
        Long completedLessons = progressRepository.countCompletedLessonsByStudentIdAndCourseId(studentId, courseId);
        Long totalLessons = lessonRepository.countLessonsByCourseId(courseId);
        Double progressPercentage = getProgressPercentage(studentId, courseId);
        
        return new StudentCourseProgress(
            student.getName(),
            student.getEmail(),
            completedLessons,
            totalLessons,
            progressPercentage
        );
    }
    
    // Get overall progress statistics for a student
    public StudentOverallProgress getStudentOverallProgress(Long studentId) {
        // Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        Long totalCompletedLessons = progressRepository.countCompletedLessonsByStudentId(studentId);
        Long totalEnrollments = enrollmentRepository.countEnrollmentsByStudentId(studentId);
        
        return new StudentOverallProgress(
            student.getName(),
            student.getEmail(),
            totalCompletedLessons,
            totalEnrollments
        );
    }
    
    // Inner class for student course progress
    public static class StudentCourseProgress {
        private String studentName;
        private String studentEmail;
        private Long completedLessons;
        private Long totalLessons;
        private Double progressPercentage;
        
        public StudentCourseProgress(String studentName, String studentEmail, Long completedLessons, 
                                   Long totalLessons, Double progressPercentage) {
            this.studentName = studentName;
            this.studentEmail = studentEmail;
            this.completedLessons = completedLessons;
            this.totalLessons = totalLessons;
            this.progressPercentage = progressPercentage;
        }
        
        public String getStudentName() { return studentName; }
        public String getStudentEmail() { return studentEmail; }
        public Long getCompletedLessons() { return completedLessons; }
        public Long getTotalLessons() { return totalLessons; }
        public Double getProgressPercentage() { return progressPercentage; }
    }
    
    // Inner class for student overall progress
    public static class StudentOverallProgress {
        private String studentName;
        private String studentEmail;
        private Long totalCompletedLessons;
        private Long totalEnrollments;
        
        public StudentOverallProgress(String studentName, String studentEmail, 
                                    Long totalCompletedLessons, Long totalEnrollments) {
            this.studentName = studentName;
            this.studentEmail = studentEmail;
            this.totalCompletedLessons = totalCompletedLessons;
            this.totalEnrollments = totalEnrollments;
        }
        
        public String getStudentName() { return studentName; }
        public String getStudentEmail() { return studentEmail; }
        public Long getTotalCompletedLessons() { return totalCompletedLessons; }
        public Long getTotalEnrollments() { return totalEnrollments; }
    }
}