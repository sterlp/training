import { Component, OnInit, ContentChild, ContentChildren, QueryList, AfterContentInit, Input, HostBinding, Renderer2, ElementRef } from '@angular/core';
import { GridItemComponent } from '../grid-item/grid-item.component';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.scss']
})
export class GridComponent implements OnInit, AfterContentInit {
  
  @ContentChildren(GridItemComponent) items : QueryList<GridItemComponent>;

  @Input() rows? = 12;
  @Input() columns? = 12;

  @Input() width? = '100%';
  @Input() height? = '100%';

  constructor(private sanitizer: DomSanitizer) { 
  }
  
  ngOnInit() {
  }
  
  ngAfterContentInit(): void {
    console.info('items:', this.items.toArray());
  }

  getColumnStyle() {
    return this.sanitizer.bypassSecurityTrustStyle(`repeat(${this.columns}, 1fr)`);
  }

  getRowStyle() {
    return this.sanitizer.bypassSecurityTrustStyle(`repeat(${this.rows}, 1fr)`);
  }
}
