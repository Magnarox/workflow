import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkflowSharedModule } from 'app/shared/shared.module';
import { WorkflowStepComponent } from './workflow-step.component';
import { WorkflowStepDetailComponent } from './workflow-step-detail.component';
import { WorkflowStepUpdateComponent } from './workflow-step-update.component';
import { WorkflowStepDeleteDialogComponent } from './workflow-step-delete-dialog.component';
import { workflowStepRoute } from './workflow-step.route';

@NgModule({
  imports: [WorkflowSharedModule, RouterModule.forChild(workflowStepRoute)],
  declarations: [WorkflowStepComponent, WorkflowStepDetailComponent, WorkflowStepUpdateComponent, WorkflowStepDeleteDialogComponent],
  entryComponents: [WorkflowStepDeleteDialogComponent]
})
export class WorkflowWorkflowStepModule {}
