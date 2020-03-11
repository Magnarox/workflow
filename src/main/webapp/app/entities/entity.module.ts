import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'workflow',
        loadChildren: () => import('./workflow/workflow.module').then(m => m.WorkflowWorkflowModule)
      },
      {
        path: 'workflow-step',
        loadChildren: () => import('./workflow-step/workflow-step.module').then(m => m.WorkflowWorkflowStepModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class WorkflowEntityModule {}
