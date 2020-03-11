import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WorkflowTestModule } from '../../../test.module';
import { WorkflowStepUpdateComponent } from 'app/entities/workflow-step/workflow-step-update.component';
import { WorkflowStepService } from 'app/entities/workflow-step/workflow-step.service';
import { WorkflowStep } from 'app/shared/model/workflow-step.model';

describe('Component Tests', () => {
  describe('WorkflowStep Management Update Component', () => {
    let comp: WorkflowStepUpdateComponent;
    let fixture: ComponentFixture<WorkflowStepUpdateComponent>;
    let service: WorkflowStepService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WorkflowTestModule],
        declarations: [WorkflowStepUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(WorkflowStepUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkflowStepUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WorkflowStepService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new WorkflowStep(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new WorkflowStep();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
