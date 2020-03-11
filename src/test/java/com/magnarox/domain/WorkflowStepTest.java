package com.magnarox.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.magnarox.web.rest.TestUtil;

public class WorkflowStepTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowStep.class);
        WorkflowStep workflowStep1 = new WorkflowStep();
        workflowStep1.setId(1L);
        WorkflowStep workflowStep2 = new WorkflowStep();
        workflowStep2.setId(workflowStep1.getId());
        assertThat(workflowStep1).isEqualTo(workflowStep2);
        workflowStep2.setId(2L);
        assertThat(workflowStep1).isNotEqualTo(workflowStep2);
        workflowStep1.setId(null);
        assertThat(workflowStep1).isNotEqualTo(workflowStep2);
    }
}
