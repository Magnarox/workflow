import { IWorkflowStep } from 'app/shared/model/workflow-step.model';

export interface IWorkflow {
  id?: number;
  name?: string;
  description?: string;
  workflowSteps?: IWorkflowStep[];
}

export class Workflow implements IWorkflow {
  constructor(public id?: number, public name?: string, public description?: string, public workflowSteps?: IWorkflowStep[]) {}
}
