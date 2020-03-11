package com.magnarox.repository.search;

import com.magnarox.domain.WorkflowStep;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link WorkflowStep} entity.
 */
public interface WorkflowStepSearchRepository extends ElasticsearchRepository<WorkflowStep, Long> {
}
