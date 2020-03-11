import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWorkflowStep } from 'app/shared/model/workflow-step.model';
import { WorkflowStepService } from './workflow-step.service';

@Component({
  templateUrl: './workflow-step-delete-dialog.component.html'
})
export class WorkflowStepDeleteDialogComponent {
  workflowStep?: IWorkflowStep;

  constructor(
    protected workflowStepService: WorkflowStepService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workflowStepService.delete(id).subscribe(() => {
      this.eventManager.broadcast('workflowStepListModification');
      this.activeModal.close();
    });
  }
}
