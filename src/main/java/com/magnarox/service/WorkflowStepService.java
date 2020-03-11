package com.magnarox.service;

import com.magnarox.domain.WorkflowStep;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link WorkflowStep}.
 */
public interface WorkflowStepService {

    /**
     * Save a workflowStep.
     *
     * @param workflowStep the entity to save.
     * @return the persisted entity.
     */
    WorkflowStep save(WorkflowStep workflowStep);

    /**
     * Get all the workflowSteps.
     *
     * @return the list of entities.
     */
    List<WorkflowStep> findAll();

    /**
     * Get the "id" workflowStep.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkflowStep> findOne(Long id);

    /**
     * Delete the "id" workflowStep.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workflowStep corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<WorkflowStep> search(String query);
}
