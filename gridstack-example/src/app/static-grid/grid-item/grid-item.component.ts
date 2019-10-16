import { Component, OnInit, Input, HostBinding, Renderer2, ElementRef } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-grid-item',
  templateUrl: './grid-item.component.html',
  styleUrls: ['./grid-item.component.scss']
})
export class GridItemComponent implements OnInit {

  @Input('item-x') x: number = 1;
  @Input('item-y') y: number = 1;
  @Input('item-width') width: number = 1;
  @Input('item-height') height: number = 1;

  constructor(private sanitizer: DomSanitizer,
    private renderer: Renderer2,
    private hostElement: ElementRef) { 
      renderer.addClass(hostElement.nativeElement, 'static-grid-item');
  }

  ngOnInit() {
    if (typeof this.x !== 'number') this.x = parseInt(this.x);
    if (typeof this.y !== 'number') this.y = parseInt(this.y);
    if (typeof this.width !== 'number') this.width = parseInt(this.width);
    if (typeof this.height !== 'number') this.height = parseInt(this.height);
  }

  doMove(direction: string, amount?: number) {
    if (typeof amount === 'undefined') amount = 1;
    if (typeof amount !== 'number') amount = parseInt(amount);

    if ("left" === direction) {
      this.x -= amount;
    } else if ("right" === direction) {
      this.x += amount;
    } else if ("top" === direction) {
      this.y -= amount;
    } else if ("bottom" === direction) {
      this.y += amount;
    } else {
      console.info(`doMove call with an unknown direction ${direction} ...`);
    }
  }

  @HostBinding('style.grid-column') get getColumnStyle() {
    return this.sanitizer.bypassSecurityTrustStyle(
      `${this.x} / span ${this.width}`
    );
  }
  @HostBinding('style.grid-row') get getRowStyle() {
    return this.sanitizer.bypassSecurityTrustStyle(
      `${this.y} / span ${this.height}`
    );
  }
}
