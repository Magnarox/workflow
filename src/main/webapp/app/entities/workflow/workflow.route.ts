import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IWorkflow, Workflow } from 'app/shared/model/workflow.model';
import { WorkflowService } from './workflow.service';
import { WorkflowComponent } from './workflow.component';
import { WorkflowDetailComponent } from './workflow-detail.component';
import { WorkflowUpdateComponent } from './workflow-update.component';

@Injectable({ providedIn: 'root' })
export class WorkflowResolve implements Resolve<IWorkflow> {
  constructor(private service: WorkflowService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkflow> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((workflow: HttpResponse<Workflow>) => {
          if (workflow.body) {
            return of(workflow.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Workflow());
  }
}

export const workflowRoute: Routes = [
  {
    path: '',
    component: WorkflowComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Workflows'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: WorkflowDetailComponent,
    resolve: {
      workflow: WorkflowResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Workflows'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: WorkflowUpdateComponent,
    resolve: {
      workflow: WorkflowResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Workflows'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: WorkflowUpdateComponent,
    resolve: {
      workflow: WorkflowResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Workflows'
    },
    canActivate: [UserRouteAccessService]
  }
];
