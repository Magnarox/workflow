import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IWorkflowStep } from 'app/shared/model/workflow-step.model';

type EntityResponseType = HttpResponse<IWorkflowStep>;
type EntityArrayResponseType = HttpResponse<IWorkflowStep[]>;

@Injectable({ providedIn: 'root' })
export class WorkflowStepService {
  public resourceUrl = SERVER_API_URL + 'api/workflow-steps';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/workflow-steps';

  constructor(protected http: HttpClient) {}

  create(workflowStep: IWorkflowStep): Observable<EntityResponseType> {
    return this.http.post<IWorkflowStep>(this.resourceUrl, workflowStep, { observe: 'response' });
  }

  update(workflowStep: IWorkflowStep): Observable<EntityResponseType> {
    return this.http.put<IWorkflowStep>(this.resourceUrl, workflowStep, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkflowStep>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkflowStep[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkflowStep[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
