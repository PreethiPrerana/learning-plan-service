package com.thbs.lms.service;

import com.thbs.lms.exception.*;
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

    private static final String NOT_FOUND_MSG = "Module not found.";
    private ModuleRepository moduleRepository;

    /**
     * Constructs a new instance of {@code LearningPlanPathService} with the
     * specified repository.
     *
     * @param moduleRepository The repository for managing learning plan
     *                         paths.
     */
    @Autowired
    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    /**
     * Saves a new module to the database with validation.
     *
     * @param module The module to be saved.
     * @return The saved module.
     * @throws InvalidDataException    If the module data is invalid.
     * @throws DuplicateEntryException If a module with the same details already
     *                                 exists.
     */
    public Module saveModule(Module module) {
        // Validates data and checks for duplicates before saving
        if (module.getStartDate() == null || module.getEndDate() == null
                || module.getTrainer() == null || module.getTrainer().isEmpty()
                || module.getCourse() == null) {
            // Throws exceptions if path data is invalid or duplicate
            throw new InvalidDataException("Invalid or incomplete data provided for creating module");
        }
        Long learningPlanId = module.getLearningPlan().getLearningPlanId();

        Optional<Module> existingEntry = moduleRepository
                .findByLearningPlanLearningPlanIdAndCourseAndStartDateAndEndDate(learningPlanId,
                        module.getCourse(), module.getStartDate(), module.getEndDate());
        if (existingEntry.isPresent()) {
            throw new DuplicateEntryException(
                    "A module with the same start date and end date for this course, attached to the same learning plan already exists.");
        }

        return moduleRepository.save(module);
    }

    /**
     * Saves a list of modules to the database with validation.
     *
     * @param modules The list of modules to be saved.
     * @return The list of saved modules.
     * @throws InvalidDataException    If any module in the list has invalid data.
     * @throws DuplicateEntryException If any module in the list already exists.
     */
    public List<Module> saveAllModules(List<Module> modules) {
        List<Module> savedModules = new ArrayList<>();
        // Validate input modules
        for (Module module : modules) {
            savedModules.add(saveModule(module));
        }
        return savedModules;
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
     * Retrieves modules handled by a trainer from the database.
     *
     * @param trainer The trainer associated with the module.
     * @return The list of modules with the specified trainer.
     * @throws InvalidDataException If the trainer is invalid.
     */
    public List<Module> getAllModulesByTrainer(String trainer) {
        // Validates trainer and retrieves paths by trainer
        if (trainer == null || trainer.isEmpty()) {
            // Throws exceptions if trainer is invalid or null
            throw new InvalidDataException("Trainer cannot be null or empty.");
        }
        return moduleRepository.findByTrainer(trainer);
    }

    /**
     * Updates the trainer of a module by its ID in the database.
     *
     * @param moduleId   The ID of the module.
     * @param newTrainer The new trainer for the module.
     * @return The updated module.
     * @throws InvalidDataException If the trainer is invalid or incomplete.
     * @throws NotFoundException    If the module with the specified ID is not
     *                              found.
     */
    public Module updateModuleTrainer(Long moduleId, String newTrainer) {
        // Validates and updates the trainer of the path
        if (newTrainer == null || newTrainer.isEmpty()) {
            // Throws exceptions if trainer is invalid or null
            throw new InvalidDataException("Invalid or Incomplete trainer value provided");
        }
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MSG));
        module.setTrainer(newTrainer);
        return moduleRepository.save(module);
    }

    /**
     * Updates the dates of a module in the database.
     *
     * @param moduleId  The ID of the module.
     * @param startDate The start date of the module.
     * @param endDate   The end date of the module.
     * @return The updated module.
     * @throws InvalidDataException If the date format is invalid or
     *                              incomplete.
     * @throws NotFoundException    If the module with
     *                              the specified ID is not found.
     */
    public Optional<Module> updateModuleDates(Long moduleId, Date startDate,
            Date endDate) {
        if (startDate == null || endDate == null) {
            // Throws exceptions if module date format is invalid or null
            throw new InvalidDataException(
                    "Invalid or incomplete date provided for updating module");
        }

        if (endDate.before(startDate)) {
            throw new InvalidDataException("End date must be after start date");
        }

        Optional<Module> optionalmodule = moduleRepository
                .findById(moduleId);
        if (optionalmodule.isPresent()) {
            Module module = optionalmodule.get();
            module.setStartDate(startDate);
            module.setEndDate(endDate);
            return Optional.of(moduleRepository.save(module));
        } else {
            throw new NotFoundException(NOT_FOUND_MSG);
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
     * Deletes a module by its ID from the database.
     *
     * @param moduleId The ID of the module to delete.
     * @throws NotFoundException If the module with the
     *                           specified ID is not found.
     */
    public void deleteModule(Long moduleId) {

        Optional<Module> module = moduleRepository.findById(moduleId);
        if (module.isPresent()) {
            moduleRepository.delete(module.get());
        } else {
            throw new NotFoundException(NOT_FOUND_MSG);
        }
    }
}
