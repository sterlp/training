import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GridStackPageComponent } from './pages/grid-stack-page/grid-stack-page.component';
import { StaticGridPageComponent } from './pages/static-grid-page/static-grid-page.component';
import { HtmlGridPageComponent } from './pages/html-grid-page/html-grid-page.component';
import { CanvasComponent } from './canvas/canvas.component';
import { CanvasGridPageComponent } from './pages/canvas-grid-page/canvas-grid-page.component';
import { NgStaticGridModule } from 'ng-static-grid';
@NgModule({
  declarations: [
    AppComponent,
    GridStackPageComponent,
    StaticGridPageComponent,
    HtmlGridPageComponent,
    CanvasComponent,
    CanvasGridPageComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgStaticGridModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
