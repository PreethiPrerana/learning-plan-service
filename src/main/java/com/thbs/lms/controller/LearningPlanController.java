package com.thbs.lms.controller;

import com.thbs.lms.model.LearningPlan;
import com.thbs.lms.service.LearningPlanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * The {@code LearningPlanController} class handles HTTP requests related to
 * learning plans.
 * It provides endpoints for adding, retrieving, and deleting learning plans, as
 * well as bulk upload functionality.
 */
@RestController
@RequestMapping("/learning-plan")
// @CrossOrigin("172.18.4.113:5173, 172.18.4.195:5173 ")
public class LearningPlanController {

    private final LearningPlanService learningPlanService;

    @Autowired
    public LearningPlanController(LearningPlanService learningPlanService) {
        this.learningPlanService = learningPlanService;
    }

    /**
     * Adds a learning plan.
     *
     * @param learningPlan the learning plan to add
     * @return a response entity containing the added learning plan
     */
    @PostMapping
    public ResponseEntity<Object> saveLearningPlan(@RequestBody LearningPlan learningPlan) {
        LearningPlan savedLearningPlan = learningPlanService.saveLearningPlan(learningPlan);
        return ResponseEntity.ok().body(savedLearningPlan);
    }

    /**
     * Retrieves all learning plans.
     *
     * @return a response entity containing a list of all learning plans
     */
    @GetMapping
    public ResponseEntity<List<LearningPlan>> getAllLearningPlans() {
        List<LearningPlan> learningPlans = learningPlanService.getAllLearningPlans();
        return ResponseEntity.ok().body(learningPlans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningPlan> getLearningPlanById(@PathVariable Long id) {
        LearningPlan learningPlan = learningPlanService.getLearningPlanById(id);
        return ResponseEntity.ok().body(learningPlan);
    }

    /**
     * Retrieves all learning plans by type.
     *
     * @param type the type of learning plans to retrieve
     * @return a response entity containing a list of learning plans for the
     *         specified type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<LearningPlan>> getLearningPlansByType(@PathVariable String type) {
        List<LearningPlan> learningPlans = learningPlanService.getLearningPlansByType(type);
        return ResponseEntity.ok().body(learningPlans);
    }

    /**
     * Retrieves all learning plans for a particular batch.
     *
     * @param batchId the batch ID
     * @return a response entity containing a list of learning plans for the
     *         specified batch
     */
    @GetMapping("/batch/{batchId}")
    public ResponseEntity<LearningPlan> getLearningPlanByBatchId(@PathVariable Set<Long> batchId) {
        LearningPlan learningPlan = learningPlanService.getLearningPlanByBatchId(batchId);
        return ResponseEntity.ok().body(learningPlan);
    }

    @PutMapping("/{id}/update-name")
    public ResponseEntity<LearningPlan> updateLearningPlanName(@PathVariable Long id, @RequestBody String newName) {
        LearningPlan updatedLearningPlan = learningPlanService.updateLearningPlanName(id, newName);
        return ResponseEntity.ok().body(updatedLearningPlan);
    }

    /**
     * Deletes a learning plan by its ID.
     *
     * @param learningPlanId the ID of the learning plan to delete
     * @return a response entity indicating the success of the deletion operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLearningPlan(@PathVariable Long id) {
        learningPlanService.deleteLearningPlan(id);
        return ResponseEntity.ok().body("LearningPlan deleted successfully");
    }
}

// // /**
// // * Retrieves learning plan DTOs.
// // *
// // * @return a response entity containing a list of learning plan DTOs
// // */
// // @GetMapping("/dto")
// // public ResponseEntity<List<LearningPlanDTO>> getAllLearningPlanPathDTOs()
// {
// // List<LearningPlanDTO> dto =
// learningPlanService.getAllLearningPlanPathDTOs();
// // return ResponseEntity.ok().body(dto);
// // }

// // /**
// // * Retrieves learning plan DTOs by batch ID.
// // *
// // * @param batchId the batch ID
// // * @return a response entity containing a list of learning plan DTOs for
// the
// // * specified batch ID
// // */
// // @GetMapping("/dto/{batchId}")
// // public ResponseEntity<List<LearningPlanDTO>>
// // getAllLearningPlanPathDTOsByBatchId(@PathVariable Long batchId) {
// // List<LearningPlanDTO> dtos =
// // learningPlanService.getAllLearningPlanPathDTOsByBatchId(batchId);
// // return ResponseEntity.ok().body(dtos);
// // }