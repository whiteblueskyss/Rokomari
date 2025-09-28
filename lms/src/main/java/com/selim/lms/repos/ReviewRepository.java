package com.selim.lms.repos;

import com.selim.lms.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBook_Id(Long bookId);
    List<Review> findByUser_Id(Long userId);
    boolean existsByIdAndUser_Id(Long id, Long userId);
}
