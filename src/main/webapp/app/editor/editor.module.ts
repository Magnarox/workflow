import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {EditorComponent} from './editor.component';
import {RouterModule} from '@angular/router';
import {editorRoute} from 'app/editor/editor.route';


@NgModule({
  declarations: [EditorComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([editorRoute])
  ]
})
export class WorkflowEditorModule {
}
