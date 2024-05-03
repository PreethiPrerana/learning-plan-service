package com.thbs.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thbs.lms.model.Course;
import com.thbs.lms.model.Module;

import java.util.List;
import java.util.Optional;

/**
 * The {@code LearningPlanPathRepository} interface provides CRUD operations for
 * the {@link com.thbs.lms.model.Module} entity.
 */
@Repository
public interface LearningPlanPathRepository extends JpaRepository<Module, Long> {
    /**
     * Retrieves a list of learning plan paths associated with a specific learning
     * plan ID.
     *
     * @param learningPlanId The ID of the learning plan.
     * @return A list of learning plan paths associated with the specified learning
     *         plan ID.
     */
    List<Module> findByLearningPlanLearningPlanId(Long learningPlanId);

    /**
     * Retrieves a list of learning plan paths by their type.
     *
     * @param type The type of the learning plan paths to retrieve.
     * @return A list of learning plan paths with the specified type.
     */
    List<Module> findByType(String type);

    /**
     * Retrieves a list of learning plan paths by their trainer.
     *
     * @param trainer The trainer of the learning plan paths to retrieve.
     * @return A list of learning plan paths with the specified trainer.
     */
    List<Module> findByTrainer(String trainer);

    /**
     * Retrieves a learning plan path by learning plan ID, course, and type.
     *
     * @param learningPlanId The ID of the learning plan.
     * @param course         The course associated with the learning plan path.
     * @param type           The type of the learning plan path.
     * @return An optional containing the learning plan path with the specified
     *         learning plan ID, course, and type, if found.
     */
    Optional<Module> findByLearningPlanLearningPlanIdAndCourseAndType(Long learningPlanId, Course course,
            String type);
}
