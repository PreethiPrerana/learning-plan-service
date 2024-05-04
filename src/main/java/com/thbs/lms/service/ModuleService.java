package com.thbs.lms.service;

import com.thbs.lms.exception.*;
import com.thbs.lms.model.Course;
import com.thbs.lms.model.Module;
import com.thbs.lms.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * The {@code moduleService} class provides methods for managing
 * modules.
 */
@Service
public class ModuleService {

    private static final String NOT_FOUND_MSG = "Learning Plan Path not found for ID: ";
    private ModuleRepository moduleRepository;

    /**
     * Constructs a new instance of {@code LearningPlanPathService} with the
     * specified repository.
     *
     * @param moduleRepository The repository for managing learning plan
     *                                   paths.
     */
    @Autowired
    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    /**
     * Saves a new learning plan path to the database with validation.
     *
     * @param module The learning plan path to be saved.
     * @return The saved learning plan path.
     * @throws InvalidmoduleDataException If the learning plan path data
     *                                              is invalid.
     * @throws DuplicatemoduleException   If a learning plan path with the
     *                                              same details already exists.
     */
    public Module saveModule(Module module) {
        // Validates data and checks for duplicates before saving
        if (module.getStartDate() == null || module.getEndDate() == null
                || module.getTrainer() == null 
                || module.getCourse() == null) {
            // Throws exceptions if path data is invalid or duplicate
            throw new InvalidModuleDataException(
                    "Invalid or incomplete data provided for creating Learning Plan Path");
        }
        Long learningPlanId = module.getLearningPlan().getLearningPlanId();
        Course course = module.getCourse();
   

        Optional<Module> existingEntry = moduleRepository
                .findByLearningPlanLearningPlanIdAndCourse(learningPlanId, course);
        if (existingEntry.isPresent()) {
            throw new DuplicateModuleException(
                    "A learning plan path with the same course, learning plan ID already exists.");
        }

        return moduleRepository.save(module);
    }

    /**
     * Saves a list of modules to the database with validation.
     *
     * @param modules The list of modules to be saved.
     * @return The list of saved modules.
     */
    public List<Module> saveAllModules(List<Module> modules) {
        List<Module> savedPaths = new ArrayList<>();
        for (Module module : modules) {
            savedPaths.add(saveModule(module));
        }
        return savedPaths;
    }

    /**
     * Retrieves all modules from the database.
     *
     * @return The list of all modules.
     */
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    /**
     * Retrieves modules by learning plan ID from the database.
     *
     * @param learningPlanId The ID of the learning plan.
     * @return The list of modules with the specified learning plan ID.
     */
    public List<Module> getAllModulesByLearningPlanId(Long learningPlanId) {
        return moduleRepository.findByLearningPlanLearningPlanId(learningPlanId);
    }

    

    /**
     * Retrieves modules by trainer from the database.
     *
     * @param trainer The trainer associated with the modules.
     * @return The list of modules with the specified trainer.
     * @throws InvalidTrainerException If the trainer is invalid.
     */
    public List<Module> getAllModulesByTrainer(String trainer) {
        // Validates trainer and retrieves paths by trainer
        if (trainer == null || trainer.isEmpty()) {
            // Throws exceptions if trainer is invalid or null
            throw new InvalidTrainerException("trainer cannot be null or empty.");
        }
        return moduleRepository.findByTrainer(trainer);
    }

    /**
     * Updates the trainer of a learning plan path by its ID in the database.
     *
     * @param moduleId     The ID of the learning plan path.
     * @param newTrainer The new trainer for the learning plan path.
     * @return The updated learning plan path.
     * @throws InvalidTrainerException           If the trainer is invalid or
     *                                           incomplete.
     * @throws moduleNotFoundException If the learning plan path with the
     *                                           specified ID is not found.
     */
    public Module updateModuleTrainer(Long moduleId, String newTrainer) {
        // Validates and updates the trainer of the path
        if (newTrainer == null || newTrainer.isEmpty()) {
            // Throws exceptions if trainer is invalid or null
            throw new InvalidTrainerException("Invalid or Incomplete trainer value provided");
        }
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(
                        NOT_FOUND_MSG + moduleId));
        module.setTrainer(newTrainer);
        return moduleRepository.save(module);
    }

    /**
     * Updates the dates of a learning plan path in the database.
     *
     * @param moduleId The ID of the learning plan path.
     * @param startDate          The start date of the learning plan path.
     * @param endDate            The end date of the learning plan path.
     * @return The updated learning plan path.
     * @throws InvalidmoduleDataException If the date format is invalid or
     *                                              incomplete.
     * @throws moduleNotFoundException    If the learning plan path with
     *                                              the specified ID is not found.
     */
    public Optional<Module> updateModuleDates(Long moduleId, Date startDate,
            Date endDate) {
        if (startDate == null || endDate == null) {
            // Throws exceptions if learning plan path date format is invalid or null
            throw new InvalidModuleDataException(
                    "Invalid or incomplete date provided for updating Learning Plan Path");
        }

        if (endDate.before(startDate)) {
            throw new InvalidModuleDataException("End date must be after start date");
        }

        Optional<Module> optionalmodule = moduleRepository
                .findById(moduleId);
        if (optionalmodule.isPresent()) {
            Module module = optionalmodule.get();
            module.setStartDate(startDate);
            module.setEndDate(endDate);
            return Optional.of(moduleRepository.save(module));
        } else {
            throw new ModuleNotFoundException(
                    NOT_FOUND_MSG + moduleId);
        }
    }

    /**
     * Deletes all modules associated with a learning plan from the
     * database.
     *
     * @param learningPlanId The ID of the learning plan.
     */
    public void deleteModulesByLearningPlanId(Long learningPlanId) {
        List<Module> modules = moduleRepository
                .findByLearningPlanLearningPlanId(learningPlanId);
        for (Module path : modules) {
            deleteModule(path.getModuleId());
        }
    }

    /**
     * Deletes a list of modules from the database.
     *
     * @param modules The list of modules to delete.
     */
    public void deleteModules(List<Module> modules) {
        moduleRepository.deleteAll(modules);
    }

    /**
     * Deletes a learning plan path by its ID from the database.
     *
     * @param moduleId The ID of the learning plan path to delete.
     * @throws moduleNotFoundException If the learning plan path with the
     *                                           specified ID is not found.
     */
    public void deleteModule(Long moduleId) {

        Optional<Module> module = moduleRepository.findById(moduleId);
        if (module.isPresent()) {
            moduleRepository.delete(module.get());
        } else {
            throw new ModuleNotFoundException(
                    NOT_FOUND_MSG + moduleId);
        }
    }
}
