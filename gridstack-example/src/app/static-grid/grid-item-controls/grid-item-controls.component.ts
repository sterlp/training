import { Component, OnInit, Input } from '@angular/core';
import { GridItemComponent } from '../grid-item/grid-item.component';

@Component({
  selector: 'app-grid-item-controls',
  templateUrl: './grid-item-controls.component.html',
  styleUrls: ['./grid-item-controls.component.scss']
})
export class GridItemControlsComponent implements OnInit {

  constructor() { }
  @Input() gridItem: GridItemComponent;

  ngOnInit() {
  }

  doMove(direction: string, amount?: number) {
    this.gridItem.doMove(direction, amount);
  }

  doChangeHeight(amount: number) {
    this.gridItem.height += amount;
  }
  doChangeWidth(amount: number) {
    this.gridItem.width += amount;
  }
}
