package com.learn.mediconnect.service;

import com.learn.mediconnect.entity.Specialization;
import com.learn.mediconnect.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    @Autowired
    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }


    public Specialization createSpecialization(Specialization specialization) {
        validateSpecializationForCreation(specialization);
        return specializationRepository.save(specialization);
    }


    public Specialization updateSpecialization(Long id, Specialization updatedSpecialization) {
        Specialization existingSpecialization = getSpecializationById(id);
        validateSpecializationForUpdate(existingSpecialization, updatedSpecialization);
        
        // Update fields
        existingSpecialization.setName(updatedSpecialization.getName());
        existingSpecialization.setDescription(updatedSpecialization.getDescription());
        
        return specializationRepository.save(existingSpecialization);
    }

    @Transactional(readOnly = true)
    public Specialization getSpecializationById(Long id) {
        return specializationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specialization not found with ID: " + id));
    }


    @Transactional(readOnly = true)
    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAll();
    }


    public void deleteSpecialization(Long id) {
        if (!specializationRepository.existsById(id)) {
            throw new IllegalArgumentException("Specialization not found with ID: " + id);
        }
        specializationRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public boolean isNameExists(String name) {
        return specializationRepository.existsByName(name);
    }

    private void validateSpecializationForCreation(Specialization specialization) {
        if (specialization == null) {
            throw new IllegalArgumentException("Specialization cannot be null");
        }
        
        if (specialization.getName() == null || specialization.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization name is required");
        }
        
        // Check if specialization with same name already exists
        if (specializationRepository.existsByName(specialization.getName().trim())) {
            throw new IllegalArgumentException("Specialization with name '" + specialization.getName().trim() + "' already exists");
        }
    }

    private void validateSpecializationForUpdate(Specialization existingSpecialization, Specialization updatedSpecialization) {
        if (updatedSpecialization == null) {
            throw new IllegalArgumentException("Updated specialization data cannot be null");
        }
        
        if (updatedSpecialization.getName() == null || updatedSpecialization.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization name is required");
        }
        
        // Check if name is being changed and if new name already exists
        String newName = updatedSpecialization.getName().trim();
        String existingName = existingSpecialization.getName().trim();
        
        if (!existingName.equals(newName)) {
            if (specializationRepository.existsByName(newName)) {
                throw new IllegalArgumentException("Specialization with name '" + newName + "' already exists");
            }
        }
    }
}