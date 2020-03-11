package com.magnarox.service.impl;

import com.magnarox.service.WorkflowStepService;
import com.magnarox.domain.WorkflowStep;
import com.magnarox.repository.WorkflowStepRepository;
import com.magnarox.repository.search.WorkflowStepSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link WorkflowStep}.
 */
@Service
@Transactional
public class WorkflowStepServiceImpl implements WorkflowStepService {

    private final Logger log = LoggerFactory.getLogger(WorkflowStepServiceImpl.class);

    private final WorkflowStepRepository workflowStepRepository;

    private final WorkflowStepSearchRepository workflowStepSearchRepository;

    public WorkflowStepServiceImpl(WorkflowStepRepository workflowStepRepository, WorkflowStepSearchRepository workflowStepSearchRepository) {
        this.workflowStepRepository = workflowStepRepository;
        this.workflowStepSearchRepository = workflowStepSearchRepository;
    }

    /**
     * Save a workflowStep.
     *
     * @param workflowStep the entity to save.
     * @return the persisted entity.
     */
    @Override
    public WorkflowStep save(WorkflowStep workflowStep) {
        log.debug("Request to save WorkflowStep : {}", workflowStep);
        WorkflowStep result = workflowStepRepository.save(workflowStep);
        workflowStepSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the workflowSteps.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowStep> findAll() {
        log.debug("Request to get all WorkflowSteps");
        return workflowStepRepository.findAll();
    }

    /**
     * Get one workflowStep by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowStep> findOne(Long id) {
        log.debug("Request to get WorkflowStep : {}", id);
        return workflowStepRepository.findById(id);
    }

    /**
     * Delete the workflowStep by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkflowStep : {}", id);
        workflowStepRepository.deleteById(id);
        workflowStepSearchRepository.deleteById(id);
    }

    /**
     * Search for the workflowStep corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowStep> search(String query) {
        log.debug("Request to search WorkflowSteps for query {}", query);
        return StreamSupport
            .stream(workflowStepSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
