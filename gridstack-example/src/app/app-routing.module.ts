import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { GridStackPageComponent } from './pages/grid-stack-page/grid-stack-page.component';
import { StaticGridPageComponent } from './pages/static-grid-page/static-grid-page.component';
import { HtmlGridPageComponent } from './pages/html-grid-page/html-grid-page.component';


const routes: Routes = [
  {path: 'grid-stack', component: GridStackPageComponent},
  {path: 'html-grid', component: HtmlGridPageComponent},
  {path: 'static-grid', component: StaticGridPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
