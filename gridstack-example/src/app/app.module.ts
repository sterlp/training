import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GridStackPageComponent } from './pages/grid-stack-page/grid-stack-page.component';
import { StaticGridPageComponent } from './pages/static-grid-page/static-grid-page.component';
import { GridComponent } from './static-grid/grid/grid.component';
import { GridItemComponent } from './static-grid/grid-item/grid-item.component';
import { HtmlGridPageComponent } from './pages/html-grid-page/html-grid-page.component';

@NgModule({
  declarations: [
    AppComponent,
    GridStackPageComponent,
    StaticGridPageComponent,
    GridComponent,
    GridItemComponent,
    HtmlGridPageComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
