package com.thbs.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * The {@code Module} class represents a module within a learning plan
 * in the learning management system.
 * It contains information such as the module ID, associated learning plan,
 * course, trainer, start date, and end date.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Module")
public class Module {
    /**
     * The unique identifier for the learning plan module.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    /**
     * The learning plan associated with the module.
     */
    @ManyToOne
    @JoinColumn(name = "learningPlanId")
    private LearningPlan learningPlan;

    /**
     * The course associated with the module.
     */
    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    /**
     * The trainer responsible for the module.
     */
    private String trainer;

    /**
     * The start date of the module.
     */
    @Temporal(TemporalType.DATE)
    private Date startDate;

    /**
     * The end date of the module.
     */
    @Temporal(TemporalType.DATE)
    private Date endDate;

    private Long batchId;
}
