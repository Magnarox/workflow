import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkflowTestModule } from '../../../test.module';
import { WorkflowStepDetailComponent } from 'app/entities/workflow-step/workflow-step-detail.component';
import { WorkflowStep } from 'app/shared/model/workflow-step.model';

describe('Component Tests', () => {
  describe('WorkflowStep Management Detail Component', () => {
    let comp: WorkflowStepDetailComponent;
    let fixture: ComponentFixture<WorkflowStepDetailComponent>;
    const route = ({ data: of({ workflowStep: new WorkflowStep(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WorkflowTestModule],
        declarations: [WorkflowStepDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(WorkflowStepDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkflowStepDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load workflowStep on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.workflowStep).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
