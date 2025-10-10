package com.learn.mediconnect.repository;

import com.learn.mediconnect.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    boolean existsByName(String name);

}

