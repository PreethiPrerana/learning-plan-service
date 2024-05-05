package com.thbs.lms.controller;

import org.springframework.web.bind.annotation.*;

import com.thbs.lms.model.Module;
import com.thbs.lms.service.ModuleService;
import com.thbs.lms.utility.DateRange;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 * The {@code moduleController} class handles HTTP requests related to
 * modules.
 * It provides endpoints for adding, retrieving, updating, and deleting learning
 * plan paths.
 */
@RestController
@RequestMapping("/module")
// @CrossOrigin("172.18.4.113:5173, 172.18.4.195:5173")
public class ModuleController {

    /**
     * The service responsible for handling business logic related to learning plan
     * paths.
     */
    private final ModuleService moduleService;

    /**
     * Constructs a new {@code moduleController} with the specified
     * service.
     *
     * @param moduleService the module service
     */
    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;

    }

    /**
     * Adds a module.
     *
     * @param module the module to add
     * @return a response entity containing the added module
     */
    @PostMapping
    public ResponseEntity<Module> createModule(@RequestBody Module module) {
        Module createdModule = moduleService.saveModule(module);
        return ResponseEntity.ok(createdModule);
    }

    /**
     * Adds multiple modules.
     *
     * @param modules the modules to add
     * @return a response entity containing a list of added modules
     */
    @PostMapping("/multiple")
    public ResponseEntity<List<Module>> createModules(@RequestBody List<Module> modules) {
        List<Module> createdModules = moduleService.saveAllModules(modules);
        return ResponseEntity.ok().body(createdModules);
    }

    /**
     * Retrieves all modules.
     *
     * @return a response entity containing a list of all modules
     */
    @GetMapping
    public ResponseEntity<List<Module>> getAllModules() {
        List<Module> modules = moduleService.getAllModules();
        return ResponseEntity.ok().body(modules);
    }

    /**
     * Retrieves all modules associated with a particular learning plan ID.
     *
     * @param learningPlanId the learning plan ID
     * @return a response entity containing a list of modules for the specified
     *         learning plan ID
     */
    @GetMapping("/learning-plan-id/{learningPlanId}")
    public ResponseEntity<List<Module>> getAllModulesByLearningPlanId(@PathVariable Long learningPlanId) {
        List<Module> modules = moduleService.getAllModulesByLearningPlanId(learningPlanId);
        return ResponseEntity.ok().body(modules);
    }

    /**
     * Retrieves all modules associated with a particular trainer.
     *
     * @param trainerName the trainer name
     * @return a response entity containing a list of modules for the
     *         specified trainer
     */
    @GetMapping("/trainer/{trainerName}")
    public ResponseEntity<List<Module>> getAllModulesByTrainer(@PathVariable String trainerName) {
        List<Module> modules = moduleService.getAllModulesByTrainer(trainerName);
        return ResponseEntity.ok().body(modules);
    }

    /**
     * Updates the trainer for a particular module.
     *
     * @param moduleId   the module ID
     * @param newTrainer the new trainer
     * @return a response entity indicating the success of the update operation
     */
    @PatchMapping("/trainer/{moduleId}")
    public ResponseEntity<String> updateTrainer(@PathVariable Long moduleId, @RequestBody String newTrainer) {
        moduleService.updateModuleTrainer(moduleId, newTrainer);
        return ResponseEntity.ok().body("Trainer updated successfully");
    }

    /**
     * Updates the start date and end date for a particular module.
     *
     * @param moduleId  the module ID
     * @param dateRange the date range containing the start and end dates
     * @return a response entity indicating the success of the update operation
     */
    @PatchMapping("/update-dates/{moduleId}")
    public ResponseEntity<String> updateDates(@PathVariable Long moduleId,
            @RequestBody DateRange dateRange) {
        moduleService.updateModuleDates(moduleId, dateRange.getStartDate(),
                dateRange.getEndDate());
        return ResponseEntity.ok().body("Date Range updated successfully.");
    }

    /**
     * Deletes a module by its ID.
     *
     * @param moduleId the ID of the module to delete
     * @return a response entity indicating the success of the deletion operation
     */
    @DeleteMapping("/{moduleId}")
    public ResponseEntity<String> deletemodule(@PathVariable Long moduleId) {
        moduleService.deleteModule(moduleId);
        return ResponseEntity.ok().body("Module deleted successfully");
    }

}