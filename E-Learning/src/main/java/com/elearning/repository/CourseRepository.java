package com.elearning.repository;

import com.elearning.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Find course by title
    Optional<Course> findByTitle(String title);
    
    // Find courses containing specific text in title or description
    @Query("SELECT c FROM Course c WHERE c.title LIKE %:searchTerm% OR c.description LIKE %:searchTerm%")
    List<Course> searchCourses(@Param("searchTerm") String searchTerm);
    
    // Get all courses ordered by creation date (newest first)
    List<Course> findAllByOrderByCreatedDateDesc();
    
    // Check if course exists by title
    boolean existsByTitle(String title);
}