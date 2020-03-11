package com.magnarox.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

import com.magnarox.domain.enumeration.StepState;

/**
 * A WorkflowStep.
 */
@Entity
@Table(name = "workflow_step")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workflowstep")
public class WorkflowStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private StepState state;

    @ManyToOne
    @JsonIgnoreProperties("workflowSteps")
    private Workflow workflow;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public WorkflowStep name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public WorkflowStep description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StepState getState() {
        return state;
    }

    public WorkflowStep state(StepState state) {
        this.state = state;
        return this;
    }

    public void setState(StepState state) {
        this.state = state;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public WorkflowStep workflow(Workflow workflow) {
        this.workflow = workflow;
        return this;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkflowStep)) {
            return false;
        }
        return id != null && id.equals(((WorkflowStep) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WorkflowStep{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
