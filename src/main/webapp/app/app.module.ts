import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import './vendor';
import {WorkflowSharedModule} from 'app/shared/shared.module';
import {WorkflowCoreModule} from 'app/core/core.module';
import {WorkflowAppRoutingModule} from './app-routing.module';
import {WorkflowHomeModule} from './home/home.module';
import {WorkflowEntityModule} from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import {MainComponent} from './layouts/main/main.component';
import {NavbarComponent} from './layouts/navbar/navbar.component';
import {FooterComponent} from './layouts/footer/footer.component';
import {PageRibbonComponent} from './layouts/profiles/page-ribbon.component';
import {ErrorComponent} from './layouts/error/error.component';
import {WorkflowEditorModule} from 'app/editor/editor.module';

@NgModule({
  imports: [
    BrowserModule,
    WorkflowSharedModule,
    WorkflowCoreModule,
    WorkflowHomeModule,
    WorkflowEditorModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    WorkflowEntityModule,
    WorkflowAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class WorkflowAppModule {
}
