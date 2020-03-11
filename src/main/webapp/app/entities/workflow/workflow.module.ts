import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkflowSharedModule } from 'app/shared/shared.module';
import { WorkflowComponent } from './workflow.component';
import { WorkflowDetailComponent } from './workflow-detail.component';
import { WorkflowUpdateComponent } from './workflow-update.component';
import { WorkflowDeleteDialogComponent } from './workflow-delete-dialog.component';
import { workflowRoute } from './workflow.route';

@NgModule({
  imports: [WorkflowSharedModule, RouterModule.forChild(workflowRoute)],
  declarations: [WorkflowComponent, WorkflowDetailComponent, WorkflowUpdateComponent, WorkflowDeleteDialogComponent],
  entryComponents: [WorkflowDeleteDialogComponent]
})
export class WorkflowWorkflowModule {}
