package com.magnarox.web.rest;

import com.magnarox.WorkflowApp;
import com.magnarox.domain.WorkflowStep;
import com.magnarox.repository.WorkflowStepRepository;
import com.magnarox.repository.search.WorkflowStepSearchRepository;
import com.magnarox.service.WorkflowStepService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.magnarox.domain.enumeration.StepState;
/**
 * Integration tests for the {@link WorkflowStepResource} REST controller.
 */
@SpringBootTest(classes = WorkflowApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class WorkflowStepResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final StepState DEFAULT_STATE = StepState.WAITING;
    private static final StepState UPDATED_STATE = StepState.RUNNING;

    @Autowired
    private WorkflowStepRepository workflowStepRepository;

    @Autowired
    private WorkflowStepService workflowStepService;

    /**
     * This repository is mocked in the com.magnarox.repository.search test package.
     *
     * @see com.magnarox.repository.search.WorkflowStepSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkflowStepSearchRepository mockWorkflowStepSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkflowStepMockMvc;

    private WorkflowStep workflowStep;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowStep createEntity(EntityManager em) {
        WorkflowStep workflowStep = new WorkflowStep()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .state(DEFAULT_STATE);
        return workflowStep;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowStep createUpdatedEntity(EntityManager em) {
        WorkflowStep workflowStep = new WorkflowStep()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .state(UPDATED_STATE);
        return workflowStep;
    }

    @BeforeEach
    public void initTest() {
        workflowStep = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkflowStep() throws Exception {
        int databaseSizeBeforeCreate = workflowStepRepository.findAll().size();

        // Create the WorkflowStep
        restWorkflowStepMockMvc.perform(post("/api/workflow-steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(workflowStep)))
            .andExpect(status().isCreated());

        // Validate the WorkflowStep in the database
        List<WorkflowStep> workflowStepList = workflowStepRepository.findAll();
        assertThat(workflowStepList).hasSize(databaseSizeBeforeCreate + 1);
        WorkflowStep testWorkflowStep = workflowStepList.get(workflowStepList.size() - 1);
        assertThat(testWorkflowStep.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkflowStep.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkflowStep.getState()).isEqualTo(DEFAULT_STATE);

        // Validate the WorkflowStep in Elasticsearch
        verify(mockWorkflowStepSearchRepository, times(1)).save(testWorkflowStep);
    }

    @Test
    @Transactional
    public void createWorkflowStepWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workflowStepRepository.findAll().size();

        // Create the WorkflowStep with an existing ID
        workflowStep.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkflowStepMockMvc.perform(post("/api/workflow-steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(workflowStep)))
            .andExpect(status().isBadRequest());

        // Validate the WorkflowStep in the database
        List<WorkflowStep> workflowStepList = workflowStepRepository.findAll();
        assertThat(workflowStepList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkflowStep in Elasticsearch
        verify(mockWorkflowStepSearchRepository, times(0)).save(workflowStep);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workflowStepRepository.findAll().size();
        // set the field null
        workflowStep.setName(null);

        // Create the WorkflowStep, which fails.

        restWorkflowStepMockMvc.perform(post("/api/workflow-steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(workflowStep)))
            .andExpect(status().isBadRequest());

        List<WorkflowStep> workflowStepList = workflowStepRepository.findAll();
        assertThat(workflowStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = workflowStepRepository.findAll().size();
        // set the field null
        workflowStep.setState(null);

        // Create the WorkflowStep, which fails.

        restWorkflowStepMockMvc.perform(post("/api/workflow-steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(workflowStep)))
            .andExpect(status().isBadRequest());

        List<WorkflowStep> workflowStepList = workflowStepRepository.findAll();
        assertThat(workflowStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkflowSteps() throws Exception {
        // Initialize the database
        workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList
        restWorkflowStepMockMvc.perform(get("/api/workflow-steps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getWorkflowStep() throws Exception {
        // Initialize the database
        workflowStepRepository.saveAndFlush(workflowStep);

        // Get the workflowStep
        restWorkflowStepMockMvc.perform(get("/api/workflow-steps/{id}", workflowStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workflowStep.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkflowStep() throws Exception {
        // Get the workflowStep
        restWorkflowStepMockMvc.perform(get("/api/workflow-steps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkflowStep() throws Exception {
        // Initialize the database
        workflowStepService.save(workflowStep);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockWorkflowStepSearchRepository);

        int databaseSizeBeforeUpdate = workflowStepRepository.findAll().size();

        // Update the workflowStep
        WorkflowStep updatedWorkflowStep = workflowStepRepository.findById(workflowStep.getId()).get();
        // Disconnect from session so that the updates on updatedWorkflowStep are not directly saved in db
        em.detach(updatedWorkflowStep);
        updatedWorkflowStep
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .state(UPDATED_STATE);

        restWorkflowStepMockMvc.perform(put("/api/workflow-steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorkflowStep)))
            .andExpect(status().isOk());

        // Validate the WorkflowStep in the database
        List<WorkflowStep> workflowStepList = workflowStepRepository.findAll();
        assertThat(workflowStepList).hasSize(databaseSizeBeforeUpdate);
        WorkflowStep testWorkflowStep = workflowStepList.get(workflowStepList.size() - 1);
        assertThat(testWorkflowStep.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkflowStep.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkflowStep.getState()).isEqualTo(UPDATED_STATE);

        // Validate the WorkflowStep in Elasticsearch
        verify(mockWorkflowStepSearchRepository, times(1)).save(testWorkflowStep);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkflowStep() throws Exception {
        int databaseSizeBeforeUpdate = workflowStepRepository.findAll().size();

        // Create the WorkflowStep

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc.perform(put("/api/workflow-steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(workflowStep)))
            .andExpect(status().isBadRequest());

        // Validate the WorkflowStep in the database
        List<WorkflowStep> workflowStepList = workflowStepRepository.findAll();
        assertThat(workflowStepList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkflowStep in Elasticsearch
        verify(mockWorkflowStepSearchRepository, times(0)).save(workflowStep);
    }

    @Test
    @Transactional
    public void deleteWorkflowStep() throws Exception {
        // Initialize the database
        workflowStepService.save(workflowStep);

        int databaseSizeBeforeDelete = workflowStepRepository.findAll().size();

        // Delete the workflowStep
        restWorkflowStepMockMvc.perform(delete("/api/workflow-steps/{id}", workflowStep.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkflowStep> workflowStepList = workflowStepRepository.findAll();
        assertThat(workflowStepList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkflowStep in Elasticsearch
        verify(mockWorkflowStepSearchRepository, times(1)).deleteById(workflowStep.getId());
    }

    @Test
    @Transactional
    public void searchWorkflowStep() throws Exception {
        // Initialize the database
        workflowStepService.save(workflowStep);
        when(mockWorkflowStepSearchRepository.search(queryStringQuery("id:" + workflowStep.getId())))
            .thenReturn(Collections.singletonList(workflowStep));
        // Search the workflowStep
        restWorkflowStepMockMvc.perform(get("/api/_search/workflow-steps?query=id:" + workflowStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
}
