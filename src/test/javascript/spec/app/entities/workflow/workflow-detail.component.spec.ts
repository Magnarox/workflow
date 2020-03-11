import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkflowTestModule } from '../../../test.module';
import { WorkflowDetailComponent } from 'app/entities/workflow/workflow-detail.component';
import { Workflow } from 'app/shared/model/workflow.model';

describe('Component Tests', () => {
  describe('Workflow Management Detail Component', () => {
    let comp: WorkflowDetailComponent;
    let fixture: ComponentFixture<WorkflowDetailComponent>;
    const route = ({ data: of({ workflow: new Workflow(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WorkflowTestModule],
        declarations: [WorkflowDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(WorkflowDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkflowDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load workflow on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.workflow).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
