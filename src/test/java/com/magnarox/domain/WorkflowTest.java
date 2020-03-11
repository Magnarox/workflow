package com.magnarox.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.magnarox.web.rest.TestUtil;

public class WorkflowTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Workflow.class);
        Workflow workflow1 = new Workflow();
        workflow1.setId(1L);
        Workflow workflow2 = new Workflow();
        workflow2.setId(workflow1.getId());
        assertThat(workflow1).isEqualTo(workflow2);
        workflow2.setId(2L);
        assertThat(workflow1).isNotEqualTo(workflow2);
        workflow1.setId(null);
        assertThat(workflow1).isNotEqualTo(workflow2);
    }
}
