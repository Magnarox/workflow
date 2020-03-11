import { IWorkflow } from 'app/shared/model/workflow.model';
import { StepState } from 'app/shared/model/enumerations/step-state.model';

export interface IWorkflowStep {
  id?: number;
  name?: string;
  description?: string;
  state?: StepState;
  workflow?: IWorkflow;
}

export class WorkflowStep implements IWorkflowStep {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public state?: StepState,
    public workflow?: IWorkflow
  ) {}
}
