import { Component, OnInit } from '@angular/core';
import { GridItemComponent } from 'src/app/static-grid/grid-item/grid-item.component';

@Component({
  selector: 'app-static-grid-page',
  templateUrl: './static-grid-page.component.html',
  styleUrls: ['./static-grid-page.component.scss']
})
export class StaticGridPageComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  doMove(comp: GridItemComponent, direction: string, amount?: number) {
    comp.doMove(direction, amount);
  }

}
