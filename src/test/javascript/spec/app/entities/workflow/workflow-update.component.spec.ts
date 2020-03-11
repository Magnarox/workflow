import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WorkflowTestModule } from '../../../test.module';
import { WorkflowUpdateComponent } from 'app/entities/workflow/workflow-update.component';
import { WorkflowService } from 'app/entities/workflow/workflow.service';
import { Workflow } from 'app/shared/model/workflow.model';

describe('Component Tests', () => {
  describe('Workflow Management Update Component', () => {
    let comp: WorkflowUpdateComponent;
    let fixture: ComponentFixture<WorkflowUpdateComponent>;
    let service: WorkflowService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WorkflowTestModule],
        declarations: [WorkflowUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(WorkflowUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkflowUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WorkflowService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Workflow(123);
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
        const entity = new Workflow();
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
