import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IWorkflowStep, WorkflowStep } from 'app/shared/model/workflow-step.model';
import { WorkflowStepService } from './workflow-step.service';
import { IWorkflow } from 'app/shared/model/workflow.model';
import { WorkflowService } from 'app/entities/workflow/workflow.service';

@Component({
  selector: 'jhi-workflow-step-update',
  templateUrl: './workflow-step-update.component.html'
})
export class WorkflowStepUpdateComponent implements OnInit {
  isSaving = false;
  workflows: IWorkflow[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    state: [null, [Validators.required]],
    workflow: []
  });

  constructor(
    protected workflowStepService: WorkflowStepService,
    protected workflowService: WorkflowService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workflowStep }) => {
      this.updateForm(workflowStep);

      this.workflowService.query().subscribe((res: HttpResponse<IWorkflow[]>) => (this.workflows = res.body || []));
    });
  }

  updateForm(workflowStep: IWorkflowStep): void {
    this.editForm.patchValue({
      id: workflowStep.id,
      name: workflowStep.name,
      description: workflowStep.description,
      state: workflowStep.state,
      workflow: workflowStep.workflow
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workflowStep = this.createFromForm();
    if (workflowStep.id !== undefined) {
      this.subscribeToSaveResponse(this.workflowStepService.update(workflowStep));
    } else {
      this.subscribeToSaveResponse(this.workflowStepService.create(workflowStep));
    }
  }

  private createFromForm(): IWorkflowStep {
    return {
      ...new WorkflowStep(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      state: this.editForm.get(['state'])!.value,
      workflow: this.editForm.get(['workflow'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkflowStep>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IWorkflow): any {
    return item.id;
  }
}
