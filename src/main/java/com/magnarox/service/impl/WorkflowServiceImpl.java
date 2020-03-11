package com.magnarox.service.impl;

import com.magnarox.service.WorkflowService;
import com.magnarox.domain.Workflow;
import com.magnarox.repository.WorkflowRepository;
import com.magnarox.repository.search.WorkflowSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Workflow}.
 */
@Service
@Transactional
public class WorkflowServiceImpl implements WorkflowService {

    private final Logger log = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    private final WorkflowRepository workflowRepository;

    private final WorkflowSearchRepository workflowSearchRepository;

    public WorkflowServiceImpl(WorkflowRepository workflowRepository, WorkflowSearchRepository workflowSearchRepository) {
        this.workflowRepository = workflowRepository;
        this.workflowSearchRepository = workflowSearchRepository;
    }

    /**
     * Save a workflow.
     *
     * @param workflow the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Workflow save(Workflow workflow) {
        log.debug("Request to save Workflow : {}", workflow);
        Workflow result = workflowRepository.save(workflow);
        workflowSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the workflows.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Workflow> findAll(Pageable pageable) {
        log.debug("Request to get all Workflows");
        return workflowRepository.findAll(pageable);
    }

    /**
     * Get one workflow by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Workflow> findOne(Long id) {
        log.debug("Request to get Workflow : {}", id);
        return workflowRepository.findById(id);
    }

    /**
     * Delete the workflow by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Workflow : {}", id);
        workflowRepository.deleteById(id);
        workflowSearchRepository.deleteById(id);
    }

    /**
     * Search for the workflow corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Workflow> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Workflows for query {}", query);
        return workflowSearchRepository.search(queryStringQuery(query), pageable);    }
}
