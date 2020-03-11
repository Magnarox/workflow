package com.magnarox.service;

import com.magnarox.domain.Workflow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Workflow}.
 */
public interface WorkflowService {

    /**
     * Save a workflow.
     *
     * @param workflow the entity to save.
     * @return the persisted entity.
     */
    Workflow save(Workflow workflow);

    /**
     * Get all the workflows.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Workflow> findAll(Pageable pageable);

    /**
     * Get the "id" workflow.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Workflow> findOne(Long id);

    /**
     * Delete the "id" workflow.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workflow corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Workflow> search(String query, Pageable pageable);
}
