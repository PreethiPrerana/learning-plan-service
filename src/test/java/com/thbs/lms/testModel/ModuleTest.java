package com.thbs.lms.testModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.thbs.lms.model.Course;
import com.thbs.lms.model.LearningPlan;
import com.thbs.lms.model.Module;

@SpringBootTest
class ModuleTest {
    @Test
    void testGettersAndSetters() {
        // Initialize a LearningPlanPath object
        Module learningPlanPath = new Module();

        // Set values using setters
        Long pathId = 1L;
        LearningPlan learningPlan = new LearningPlan();
        Course course = new Course();
        String type = "Test Type";
        String trainer = "Test Trainer";
        Date startDate = new Date();
        Date endDate = new Date();

        learningPlanPath.setModuleId(pathId);
        learningPlanPath.setLearningPlan(learningPlan);
        learningPlanPath.setCourse(course);
        learningPlanPath.setTrainer(trainer);
        learningPlanPath.setStartDate(startDate);
        learningPlanPath.setEndDate(endDate);

        // Test getters
        assertEquals(pathId, learningPlanPath.getModuleId());
        assertEquals(learningPlan, learningPlanPath.getLearningPlan());
        assertEquals(course, learningPlanPath.getCourse());
        assertEquals(trainer, learningPlanPath.getTrainer());
        assertEquals(startDate, learningPlanPath.getStartDate());
        assertEquals(endDate, learningPlanPath.getEndDate());
    }

    @Test
    void testNoArgsConstructor() {
        Module learningPlanPath = new Module();
        assertNotNull(learningPlanPath);
        assertNull(learningPlanPath.getModuleId());
        assertNull(learningPlanPath.getLearningPlan());
        assertNull(learningPlanPath.getCourse());
        assertNull(learningPlanPath.getTrainer());
        assertNull(learningPlanPath.getStartDate());
        assertNull(learningPlanPath.getEndDate());
    }

    @Test
    void testAllArgsConstructor() {
        Long pathId = 1L;
        LearningPlan learningPlan = new LearningPlan();
        Course course = new Course();
        String trainer = "Test Trainer";
        Date startDate = new Date();
        Date endDate = new Date();

        Module learningPlanPath = new Module(pathId, learningPlan, course, trainer, startDate,
                endDate);
        assertNotNull(learningPlanPath);
        assertEquals(pathId, learningPlanPath.getModuleId());
        assertEquals(learningPlan, learningPlanPath.getLearningPlan());
        assertEquals(course, learningPlanPath.getCourse());
        assertEquals(trainer, learningPlanPath.getTrainer());
        assertEquals(startDate, learningPlanPath.getStartDate());
        assertEquals(endDate, learningPlanPath.getEndDate());
    }
}
