import { Component, OnInit, ContentChildren, QueryList,
         AfterContentInit, Input, ElementRef, ViewChild,
         AfterViewChecked } from '@angular/core';
import { GridItemComponent } from '../grid-item/grid-item.component';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.scss']
})
export class GridComponent implements OnInit, AfterContentInit, AfterViewChecked {
  @ViewChild('grid', {static: true}) grid: ElementRef;
  @ContentChildren(GridItemComponent) items: QueryList<GridItemComponent>;

  @Input() rows ? = 12;
  @Input() columns ? = 12;

  @Input() width ? = '100%';
  @Input() height ? = '100%';

  constructor(private sanitizer: DomSanitizer) { }

  ngOnInit() {}

  ngAfterContentInit(): void {
  }
  ngAfterViewChecked(): void {
    this.doPosition();
  }

  doPosition() {
    const w = this.calcWidth();
    const h = this.calcHeight();
    this.items.forEach(item => {
      item.doPosition(w, h);
    });
  }

  calcWidth(): number {
    return 100 / this.columns;
  }
  calcHeight(): number {
    return 100 / this.rows;
  }
}
