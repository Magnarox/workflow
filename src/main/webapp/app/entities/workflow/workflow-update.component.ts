import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IWorkflow, Workflow } from 'app/shared/model/workflow.model';
import { WorkflowService } from './workflow.service';

@Component({
  selector: 'jhi-workflow-update',
  templateUrl: './workflow-update.component.html'
})
export class WorkflowUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: []
  });

  constructor(protected workflowService: WorkflowService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workflow }) => {
      this.updateForm(workflow);
    });
  }

  updateForm(workflow: IWorkflow): void {
    this.editForm.patchValue({
      id: workflow.id,
      name: workflow.name,
      description: workflow.description
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workflow = this.createFromForm();
    if (workflow.id !== undefined) {
      this.subscribeToSaveResponse(this.workflowService.update(workflow));
    } else {
      this.subscribeToSaveResponse(this.workflowService.create(workflow));
    }
  }

  private createFromForm(): IWorkflow {
    return {
      ...new Workflow(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkflow>>): void {
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
}
