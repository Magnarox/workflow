import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWorkflow } from 'app/shared/model/workflow.model';
import { WorkflowService } from './workflow.service';

@Component({
  templateUrl: './workflow-delete-dialog.component.html'
})
export class WorkflowDeleteDialogComponent {
  workflow?: IWorkflow;

  constructor(protected workflowService: WorkflowService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workflowService.delete(id).subscribe(() => {
      this.eventManager.broadcast('workflowListModification');
      this.activeModal.close();
    });
  }
}
