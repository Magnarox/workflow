import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkflowStep } from 'app/shared/model/workflow-step.model';

@Component({
  selector: 'jhi-workflow-step-detail',
  templateUrl: './workflow-step-detail.component.html'
})
export class WorkflowStepDetailComponent implements OnInit {
  workflowStep: IWorkflowStep | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workflowStep }) => (this.workflowStep = workflowStep));
  }

  previousState(): void {
    window.history.back();
  }
}
