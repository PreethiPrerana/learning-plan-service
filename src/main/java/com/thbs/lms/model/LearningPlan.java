package com.thbs.lms.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LearningPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long learningPlanId;

    private String learningPlanName;
    private String type;

    // @ElementCollection
    // @CollectionTable(name = "LearningPlan_Batch", joinColumns = @JoinColumn(name = "learning_plan_id"))
    // @Column(name = "batch_id")
    // private Set<Long> batchIds = new HashSet<>();

    // public void addBatchId(Set<Long> set) {
    //     batchIds.addAll(set);
    // }
}
