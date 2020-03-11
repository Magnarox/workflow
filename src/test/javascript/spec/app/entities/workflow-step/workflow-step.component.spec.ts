import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { WorkflowTestModule } from '../../../test.module';
import { WorkflowStepComponent } from 'app/entities/workflow-step/workflow-step.component';
import { WorkflowStepService } from 'app/entities/workflow-step/workflow-step.service';
import { WorkflowStep } from 'app/shared/model/workflow-step.model';

describe('Component Tests', () => {
  describe('WorkflowStep Management Component', () => {
    let comp: WorkflowStepComponent;
    let fixture: ComponentFixture<WorkflowStepComponent>;
    let service: WorkflowStepService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WorkflowTestModule],
        declarations: [WorkflowStepComponent]
      })
        .overrideTemplate(WorkflowStepComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkflowStepComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WorkflowStepService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new WorkflowStep(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.workflowSteps && comp.workflowSteps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
