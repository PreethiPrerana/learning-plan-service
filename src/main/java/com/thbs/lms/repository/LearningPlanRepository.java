package com.thbs.lms.repository;

import com.thbs.lms.model.LearningPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * The {@code LearningPlanRepository} interface provides CRUD operations for the
 * {@link com.thbs.lms.model.LearningPlan} entity.
 */
@Repository
public interface LearningPlanRepository extends JpaRepository<LearningPlan, Long> {

    LearningPlan findByLearningPlanNameAndType(String learningPlanName, String type);

    LearningPlan findByBatchIdsContaining(Set<Long> batchIds);

    List<LearningPlan> findByType(String type);

}
