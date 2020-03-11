package com.magnarox.web.rest;

import com.magnarox.WorkflowApp;
import com.magnarox.domain.Workflow;
import com.magnarox.repository.WorkflowRepository;
import com.magnarox.repository.search.WorkflowSearchRepository;
import com.magnarox.service.WorkflowService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

/**
 * Integration tests for the {@link WorkflowResource} REST controller.
 */
@SpringBootTest(classes = WorkflowApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class WorkflowResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private WorkflowService workflowService;

    /**
     * This repository is mocked in the com.magnarox.repository.search test package.
     *
     * @see com.magnarox.repository.search.WorkflowSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkflowSearchRepository mockWorkflowSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkflowMockMvc;

    private Workflow workflow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workflow createEntity(EntityManager em) {
        Workflow workflow = new Workflow()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return workflow;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workflow createUpdatedEntity(EntityManager em) {
        Workflow workflow = new Workflow()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return workflow;
    }

    @BeforeEach
    public void initTest() {
        workflow = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkflow() throws Exception {
        int databaseSizeBeforeCreate = workflowRepository.findAll().size();

        // Create the Workflow
        restWorkflowMockMvc.perform(post("/api/workflows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(workflow)))
            .andExpect(status().isCreated());

        // Validate the Workflow in the database
        List<Workflow> workflowList = workflowRepository.findAll();
        assertThat(workflowList).hasSize(databaseSizeBeforeCreate + 1);
        Workflow testWorkflow = workflowList.get(workflowList.size() - 1);
        assertThat(testWorkflow.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkflow.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Workflow in Elasticsearch
        verify(mockWorkflowSearchRepository, times(1)).save(testWorkflow);
    }

    @Test
    @Transactional
    public void createWorkflowWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workflowRepository.findAll().size();

        // Create the Workflow with an existing ID
        workflow.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkflowMockMvc.perform(post("/api/workflows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(workflow)))
            .andExpect(status().isBadRequest());

        // Validate the Workflow in the database
        List<Workflow> workflowList = workflowRepository.findAll();
        assertThat(workflowList).hasSize(databaseSizeBeforeCreate);

        // Validate the Workflow in Elasticsearch
        verify(mockWorkflowSearchRepository, times(0)).save(workflow);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workflowRepository.findAll().size();
        // set the field null
        workflow.setName(null);

        // Create the Workflow, which fails.

        restWorkflowMockMvc.perform(post("/api/workflows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(workflow)))
            .andExpect(status().isBadRequest());

        List<Workflow> workflowList = workflowRepository.findAll();
        assertThat(workflowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkflows() throws Exception {
        // Initialize the database
        workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList
        restWorkflowMockMvc.perform(get("/api/workflows?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflow.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getWorkflow() throws Exception {
        // Initialize the database
        workflowRepository.saveAndFlush(workflow);

        // Get the workflow
        restWorkflowMockMvc.perform(get("/api/workflows/{id}", workflow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workflow.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingWorkflow() throws Exception {
        // Get the workflow
        restWorkflowMockMvc.perform(get("/api/workflows/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkflow() throws Exception {
        // Initialize the database
        workflowService.save(workflow);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockWorkflowSearchRepository);

        int databaseSizeBeforeUpdate = workflowRepository.findAll().size();

        // Update the workflow
        Workflow updatedWorkflow = workflowRepository.findById(workflow.getId()).get();
        // Disconnect from session so that the updates on updatedWorkflow are not directly saved in db
        em.detach(updatedWorkflow);
        updatedWorkflow
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restWorkflowMockMvc.perform(put("/api/workflows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorkflow)))
            .andExpect(status().isOk());

        // Validate the Workflow in the database
        List<Workflow> workflowList = workflowRepository.findAll();
        assertThat(workflowList).hasSize(databaseSizeBeforeUpdate);
        Workflow testWorkflow = workflowList.get(workflowList.size() - 1);
        assertThat(testWorkflow.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkflow.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Workflow in Elasticsearch
        verify(mockWorkflowSearchRepository, times(1)).save(testWorkflow);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkflow() throws Exception {
        int databaseSizeBeforeUpdate = workflowRepository.findAll().size();

        // Create the Workflow

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowMockMvc.perform(put("/api/workflows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(workflow)))
            .andExpect(status().isBadRequest());

        // Validate the Workflow in the database
        List<Workflow> workflowList = workflowRepository.findAll();
        assertThat(workflowList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Workflow in Elasticsearch
        verify(mockWorkflowSearchRepository, times(0)).save(workflow);
    }

    @Test
    @Transactional
    public void deleteWorkflow() throws Exception {
        // Initialize the database
        workflowService.save(workflow);

        int databaseSizeBeforeDelete = workflowRepository.findAll().size();

        // Delete the workflow
        restWorkflowMockMvc.perform(delete("/api/workflows/{id}", workflow.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Workflow> workflowList = workflowRepository.findAll();
        assertThat(workflowList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Workflow in Elasticsearch
        verify(mockWorkflowSearchRepository, times(1)).deleteById(workflow.getId());
    }

    @Test
    @Transactional
    public void searchWorkflow() throws Exception {
        // Initialize the database
        workflowService.save(workflow);
        when(mockWorkflowSearchRepository.search(queryStringQuery("id:" + workflow.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(workflow), PageRequest.of(0, 1), 1));
        // Search the workflow
        restWorkflowMockMvc.perform(get("/api/_search/workflows?query=id:" + workflow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflow.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
