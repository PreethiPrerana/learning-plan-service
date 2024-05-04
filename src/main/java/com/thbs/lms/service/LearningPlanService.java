package com.thbs.lms.service;

import com.thbs.lms.exception.*;
import com.thbs.lms.model.LearningPlan;
import com.thbs.lms.repository.LearningPlanRepository;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The {@code LearningPlanService} class provides methods for managing learning
 * plans.
 */
@Service
public class LearningPlanService {

    private final LearningPlanRepository learningPlanRepository;

    /**
     * Constructs a new instance of {@code LearningPlanService} with the specified
     * dependencies.
     *
     * @param learningPlanRepository The repository for managing learning plans.
     */
    @Autowired
    public LearningPlanService(LearningPlanRepository learningPlanRepository) {
        this.learningPlanRepository = learningPlanRepository;
    }

    /**
     * Saves a new learning plan to the database.
     *
     * @param learningPlan The learning plan to be saved.
     * @return The saved learning plan.
     * @throws DuplicateLearningPlanException If a learning plan already exists for
     *                                        the given batch.
     * @throws InvalidLearningPlanException   If the batch ID or learning plan type
     *                                        is null.
     */
    public LearningPlan saveLearningPlan(LearningPlan learningPlan) {
        // Check if the batch ID is already attached to a learning plan
        LearningPlan existingLearningPlan = learningPlanRepository.findByBatchIdsContaining(learningPlan.getBatchIds());
        if (existingLearningPlan != null) {
            throw new DuplicateLearningPlanException("Learning plan for this batch already exists");
        }

        // Check if a learning plan with the same name and type exists
        LearningPlan duplicateLearningPlan = learningPlanRepository
                .findByLearningPlanNameAndType(learningPlan.getLearningPlanName(), learningPlan.getType());
        if (duplicateLearningPlan != null) {
            // Check if the batch ID is different
            if (!duplicateLearningPlan.getBatchIds().equals(learningPlan.getBatchIds())) {
                // Add batch ID to existing learning plan
                duplicateLearningPlan.addBatchId(learningPlan.getBatchIds());
                return learningPlanRepository.save(duplicateLearningPlan);
            }
            return duplicateLearningPlan; // Return existing learning plan without creating duplicate
        }

        // No duplicate learning plan found, save the new learning plan
        return learningPlanRepository.save(learningPlan);
    }

    /**
     * Retrieves all learning plans from the database.
     *
     * @return The list of all learning plans.
     */
    public List<LearningPlan> getAllLearningPlans() {
        return learningPlanRepository.findAll();
    }

    /**
     * Retrieves a learning plan by its ID from the database.
     *
     * @param id The ID of the learning plan.
     * @return The learning plan with the specified ID.
     * @throws LearningPlanNotFoundException If the learning plan with the specified
     *                                       ID is not found.
     */
    public LearningPlan getLearningPlanById(Long id) {
        return learningPlanRepository.findById(id)
                .orElseThrow(() -> new LearningPlanNotFoundException("Learning plan not found."));
    }

    /**
     * Retrieves learning plans by type from the database.
     *
     * @param type The type of the learning plans.
     * @return The list of learning plans with the specified type.
     * @throws InvalidTypeException          If the type is null.
     * @throws LearningPlanNotFoundException If no learning plans are found with the
     *                                       specified type.
     */
    public List<LearningPlan> getLearningPlansByType(String type) {
        // Validates type and retrieves learning plans by type
        if (type == null || type.isEmpty()) {
            throw new InvalidTypeException("Learning Plan Type cannot be null");
        }
        List<LearningPlan> learningPlan = learningPlanRepository.findByType(type);
        if (!learningPlan.isEmpty()) {
            return learningPlan;
        } else {
            // Throws exception if type is invalid or no learning plans found
            throw new LearningPlanNotFoundException("Learning plan not found.");
        }
    }

    /**
     * Retrieves learning plan associated with the batch ID from the database.
     *
     * @param batchID The ID of the batch.
     * @return The learning plan associated with the specified batch ID.
     * @throws InvalidBatchException         If the batch ID is null.
     * @throws LearningPlanNotFoundException If no learning plans are found for the
     *                                       specified batch ID.
     */
    public LearningPlan getLearningPlanByBatchId(Set<Long> batchId) {
        // Validates batch ID and retrieves learning plans by batch ID
        if (batchId == null) {
            // Throws exception if batch ID is invalid or no learning plans found
            throw new InvalidBatchException("Batch ID cannot be null");
        }
        LearningPlan learningPlan = learningPlanRepository.findByBatchIdsContaining(batchId);
        if (learningPlan != null) {
            return learningPlan;
        } else {
            throw new LearningPlanNotFoundException("Learning plan not found.");
        }
    }

    /**
     * Updates the name of a learning plan.
     *
     * @param id      The ID of the learning plan to update.
     * @param newName The new name for the learning plan.
     * @return The updated learning plan.
     * @throws LearningPlanNotFoundException If the learning plan with the specified
     *                                       ID is not found.
     */
    public LearningPlan updateLearningPlanName(Long id, String newName) {
        LearningPlan learningPlan = learningPlanRepository.findById(id)
                .orElseThrow(() -> new LearningPlanNotFoundException("Learning plan not found."));
        learningPlan.setLearningPlanName(newName);
        return learningPlanRepository.save(learningPlan);
    }

    /**
     * Deletes a learning plan by its ID from the database along with its associated
     * paths.
     *
     * @param id The ID of the learning plan to delete.
     * @throws LearningPlanNotFoundException If the learning plan with the specified
     *                                       ID is not found.
     */
    public void deleteLearningPlan(Long id) {
        if (!learningPlanRepository.existsById(id)) {
            throw new LearningPlanNotFoundException("Learning plan not found.");
        }
        learningPlanRepository.deleteById(id);
    }
}

// // /**
// // * Retrieves all learning plan DTOs (Data Transfer Objects) from the
// database.
// // *
// // * @return The list of all learning plan DTOs.
// // */
// // public List<LearningPlanDTO> getAllLearningPlanPathDTOs() {
// // List<LearningPlanDTO> dtos = new ArrayList<>();
// // List<LearningPlan> learningPlans = learningPlanRepository.findAll();
// // for (LearningPlan learningPlan : learningPlans) {
// // LearningPlanDTO dto = convertToDTO(learningPlan.getLearningPlanId());
// // dtos.add(dto);
// // }
// // return dtos;
// // }

// // /**
// // * Retrieves all learning plan DTOs (Data Transfer Objects) filtered by
// batch
// // ID
// // * from the database.
// // *
// // * @param batchId The ID of the batch.
// // * @return The list of learning plan DTOs filtered by batch ID.
// // */
// // public List<LearningPlanDTO> getAllLearningPlanPathDTOsByBatchId(Long
// // batchId) {
// // List<LearningPlanDTO> dtoByBatch = new ArrayList<>();
// // List<LearningPlanDTO> allDTO = getAllLearningPlanPathDTOs();
// // for (LearningPlanDTO DTO : allDTO) {
// // if (batchId.equals(DTO.getBatchId()))
// // dtoByBatch.add(DTO);
// // }

// // return dtoByBatch;
// // }

// // public Long getBatchIdByLearningPlanId(Long learningPlanId) {
// // LearningPlan learningPlan =
// learningPlanRepository.findById(learningPlanId)
// // .orElse(null);
// // if (learningPlan != null) {
// // return learningPlan.getBatchId();
// // } else {
// // // Handle the case where the learning plan with the given ID is not found
// // return null; // Or throw an exception, depending on your requirements
// // }
// // }

// // /**
// // * Converts a learning plan to a DTO (Data Transfer Object).
// // *
// // * @param learningPlanId The ID of the learning plan.
// // * @return The DTO representing the learning plan.
// // */
// // public LearningPlanDTO convertToDTO(Long learningPlanId) {
// // LearningPlanDTO dto = new LearningPlanDTO();

// // LearningPlan learningPlan = getLearningPlanById(learningPlanId);
// // dto.setBatchId(learningPlan.getBatchId());
// // dto.setLearningPlanId(learningPlanId);

// // List<Module> relatedPaths = learningPlanPathRepository
// // .findByLearningPlanLearningPlanId(learningPlanId);
// // List<PathDTO> paths = new ArrayList<>();

// // for (Module path : relatedPaths) {
// // if (path.getType().equalsIgnoreCase("course")) {
// // PathDTO pathDTO = new PathDTO();
// // pathDTO.setLearningPlanPathId(path.getPathId());
// // pathDTO.setType(path.getType());
// // pathDTO.setTrainer(path.getTrainer());
// // pathDTO.setStartDate(path.getStartDate());
// // pathDTO.setEndDate(path.getEndDate());

// // Course course = path.getCourse();
// // CourseDTO courseDTO = courseService.convertToDTO(course);
// // pathDTO.setCourse(courseDTO);

// // paths.add(pathDTO);
// // }

// // }

// // dto.setPath(paths);
// // return dto;
// // }