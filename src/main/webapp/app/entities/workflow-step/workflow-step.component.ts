import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkflowStep } from 'app/shared/model/workflow-step.model';
import { WorkflowStepService } from './workflow-step.service';
import { WorkflowStepDeleteDialogComponent } from './workflow-step-delete-dialog.component';

@Component({
  selector: 'jhi-workflow-step',
  templateUrl: './workflow-step.component.html'
})
export class WorkflowStepComponent implements OnInit, OnDestroy {
  workflowSteps?: IWorkflowStep[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected workflowStepService: WorkflowStepService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.workflowStepService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IWorkflowStep[]>) => (this.workflowSteps = res.body || []));
      return;
    }

    this.workflowStepService.query().subscribe((res: HttpResponse<IWorkflowStep[]>) => (this.workflowSteps = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInWorkflowSteps();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IWorkflowStep): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInWorkflowSteps(): void {
    this.eventSubscriber = this.eventManager.subscribe('workflowStepListModification', () => this.loadAll());
  }

  delete(workflowStep: IWorkflowStep): void {
    const modalRef = this.modalService.open(WorkflowStepDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.workflowStep = workflowStep;
  }
}
