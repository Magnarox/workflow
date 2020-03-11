import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IWorkflowStep, WorkflowStep } from 'app/shared/model/workflow-step.model';
import { WorkflowStepService } from './workflow-step.service';
import { WorkflowStepComponent } from './workflow-step.component';
import { WorkflowStepDetailComponent } from './workflow-step-detail.component';
import { WorkflowStepUpdateComponent } from './workflow-step-update.component';

@Injectable({ providedIn: 'root' })
export class WorkflowStepResolve implements Resolve<IWorkflowStep> {
  constructor(private service: WorkflowStepService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkflowStep> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((workflowStep: HttpResponse<WorkflowStep>) => {
          if (workflowStep.body) {
            return of(workflowStep.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WorkflowStep());
  }
}

export const workflowStepRoute: Routes = [
  {
    path: '',
    component: WorkflowStepComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'WorkflowSteps'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: WorkflowStepDetailComponent,
    resolve: {
      workflowStep: WorkflowStepResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'WorkflowSteps'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: WorkflowStepUpdateComponent,
    resolve: {
      workflowStep: WorkflowStepResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'WorkflowSteps'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: WorkflowStepUpdateComponent,
    resolve: {
      workflowStep: WorkflowStepResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'WorkflowSteps'
    },
    canActivate: [UserRouteAccessService]
  }
];
