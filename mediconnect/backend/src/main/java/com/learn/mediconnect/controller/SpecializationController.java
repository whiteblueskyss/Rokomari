package com.learn.mediconnect.controller;

import com.learn.mediconnect.dto.SpecializationDTO;
import com.learn.mediconnect.entity.Specialization;
import com.learn.mediconnect.service.SpecializationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/specializations")
@CrossOrigin(origins = "*")
public class SpecializationController {

    private final SpecializationService specializationService;

    @Autowired
    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @PostMapping
    public ResponseEntity<SpecializationDTO> createSpecialization(@Valid @RequestBody SpecializationDTO specializationDTO) {
        Specialization specialization = convertToEntity(specializationDTO);
        Specialization createdSpecialization = specializationService.createSpecialization(specialization);
        SpecializationDTO responseDTO = convertToDTO(createdSpecialization);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecializationDTO> getSpecializationById(@PathVariable Long id) {
        Specialization specialization = specializationService.getSpecializationById(id);
        SpecializationDTO specializationDTO = convertToDTO(specialization);
        return ResponseEntity.ok(specializationDTO);
    }

    @GetMapping
    public ResponseEntity<List<SpecializationDTO>> getAllSpecializations() {
        List<Specialization> specializations = specializationService.getAllSpecializations();
        List<SpecializationDTO> specializationDTOs = specializations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(specializationDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecializationDTO> updateSpecialization(@PathVariable Long id, @Valid @RequestBody SpecializationDTO specializationDTO) {
        Specialization specialization = convertToEntity(specializationDTO);
        Specialization updatedSpecialization = specializationService.updateSpecialization(id, specialization);
        SpecializationDTO responseDTO = convertToDTO(updatedSpecialization);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialization(@PathVariable Long id) {
        specializationService.deleteSpecialization(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-name")
    public ResponseEntity<Boolean> checkNameExists(@RequestParam String name) {
        boolean exists = specializationService.isNameExists(name);
        return ResponseEntity.ok(exists);
    }

    // Helper methods for entity-DTO conversion
    private Specialization convertToEntity(SpecializationDTO dto) {
        Specialization specialization = new Specialization();
        specialization.setId(dto.getId());
        specialization.setName(dto.getName());
        specialization.setDescription(dto.getDescription());
        return specialization;
    }

    private SpecializationDTO convertToDTO(Specialization specialization) {
        SpecializationDTO dto = new SpecializationDTO();
        dto.setId(specialization.getId());
        dto.setName(specialization.getName());
        dto.setDescription(specialization.getDescription());
        return dto;
    }
}