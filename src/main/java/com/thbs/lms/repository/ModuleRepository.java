package com.thbs.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thbs.lms.model.Course;
import com.thbs.lms.model.Module;

import java.util.List;
import java.util.Optional;

/**
 * The {@code moduleRepository} interface provides CRUD operations for
 * the {@link com.thbs.lms.model.Module} entity.
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    /**
     * Retrieves a list of modules associated with a specific learning
     * plan ID.
     *
     * @param learningPlanId The ID of the learning plan.
     * @return A list of modules associated with the specified learning
     *         plan ID.
     */
    List<Module> findByLearningPlanLearningPlanId(Long learningPlanId);

   
    /**
     * Retrieves a list of modules by their trainer.
     *
     * @param trainer The trainer of the modules to retrieve.
     * @return A list of modules with the specified trainer.
     */
    List<Module> findByTrainer(String trainer);

    /**
     * Retrieves a module by learning plan ID, course 
     *
     * @param learningPlanId The ID of the learning plan.
     * @param course         The course associated with the module.
     * 
     * @return An optional containing the module with the specified
     *         learning plan ID, course  if found.
     */
    Optional<Module> findByLearningPlanLearningPlanIdAndCourse(Long learningPlanId, Course course);
}
