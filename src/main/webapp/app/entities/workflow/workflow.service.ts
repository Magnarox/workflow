import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IWorkflow } from 'app/shared/model/workflow.model';

type EntityResponseType = HttpResponse<IWorkflow>;
type EntityArrayResponseType = HttpResponse<IWorkflow[]>;

@Injectable({ providedIn: 'root' })
export class WorkflowService {
  public resourceUrl = SERVER_API_URL + 'api/workflows';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/workflows';

  constructor(protected http: HttpClient) {}

  create(workflow: IWorkflow): Observable<EntityResponseType> {
    return this.http.post<IWorkflow>(this.resourceUrl, workflow, { observe: 'response' });
  }

  update(workflow: IWorkflow): Observable<EntityResponseType> {
    return this.http.put<IWorkflow>(this.resourceUrl, workflow, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkflow>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkflow[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkflow[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
