import {Route} from '@angular/router';

import {EditorComponent} from './editor.component';
import {Authority} from 'app/shared/constants/authority.constants';
import {UserRouteAccessService} from 'app/core/auth/user-route-access-service';

export const editorRoute: Route = {
  path: 'editor',
  component: EditorComponent,
  data: {
    authorities: [Authority.USER],
    pageTitle: 'Workflow Editor'
  },
  canActivate: [UserRouteAccessService]
};
