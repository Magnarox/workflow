package com.magnarox.repository.search;

import com.magnarox.domain.Workflow;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Workflow} entity.
 */
public interface WorkflowSearchRepository extends ElasticsearchRepository<Workflow, Long> {
}
