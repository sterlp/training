import { Component, OnInit, Input, HostBinding, Renderer2, ElementRef, Output, EventEmitter } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Attribute, NUMBER_PARSER } from '../attribute.model';

@Component({
  selector: 'app-grid-item',
  templateUrl: './grid-item.component.html',
  styleUrls: ['./grid-item.component.scss']
})
/* tslint:disable:variable-name no-output-rename no-input-rename curly*/
export class GridItemComponent implements OnInit {

  private _x = new Attribute<number>(NUMBER_PARSER, 1);
  @Output('item-xChange') xChange = this._x.eventEmitter;
  @Input('item-x') set x(val: number) { this._x.value = val; }
  get x(): number {return this._x.value; }

  _y = new Attribute<number>(NUMBER_PARSER, 1);
  @Output('item-yChange') yChange = this._y.eventEmitter;
  @Input('item-y') set y(value: number) { this._y.value = value; }
  get y(): number {return this._y.value; }

  @Input('item-width') width = 1;
  @Input('item-height') height = 1;

  constructor(private sanitizer: DomSanitizer,
              renderer: Renderer2,
              hostElement: ElementRef) {
      renderer.addClass(hostElement.nativeElement, 'static-grid-item');
  }

  ngOnInit() {
    if (typeof this.width !== 'number') this.width = parseInt(this.width, 10);
    if (typeof this.height !== 'number') this.height = parseInt(this.height, 10);
  }

  doMove(direction: string, amount?: number): void {
    if (typeof amount === 'undefined') amount = 1;
    else if (typeof amount !== 'number') amount = parseInt(amount, 10);

    if ('left' === direction) {
      this.x -= amount;
    } else if ('right' === direction) {
      this.x += amount;
    } else if ('top' === direction) {
      this.y -= amount;
    } else if ('bottom' === direction) {
      this.y += amount;
    } else {
      console.warn(`doMove call with an unknown direction: ${direction}`);
    }
  }

  @HostBinding('style.grid-column')
  get columnStyle() {
    return this.sanitizer.bypassSecurityTrustStyle(
      `${this.x} / span ${this.width}`
    );
  }
  @HostBinding('style.grid-row')
  get rowStyle() {
    return this.sanitizer.bypassSecurityTrustStyle(
      `${this.y} / span ${this.height}`
    );
  }
}
