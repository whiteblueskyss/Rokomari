package com.selim.lms.repos;

import com.selim.lms.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);

    List<Author> findByBooks_Id(Long bookId);

    // Find authors by a specific criteria (renamed to avoid conflict)
    List<Author> findByIdIn(List<Long> ids);
}
