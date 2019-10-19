import { Component, OnInit, HostListener, ElementRef, ViewChild, AfterContentInit, Input } from '@angular/core';

@Component({
  selector: 'app-canvas',
  templateUrl: './canvas.component.html',
  styleUrls: ['./canvas.component.scss']
})
export class CanvasComponent implements OnInit, AfterContentInit {

  @ViewChild('canvas', {static: true}) canvas: ElementRef;
  @Input() hostElement ?: ElementRef;
  @Input() canvasZIndex ? = 0;

  constructor(private widgetEl: ElementRef) { }

  ngOnInit() {
  }

  ngAfterContentInit(): void {
    if (!this.hostElement) {
      if (this.widgetEl.nativeElement.parentNode) {
        this.hostElement = new ElementRef(this.widgetEl.nativeElement.parentNode);
      } else {
        this.hostElement = this.widgetEl;
      }
    }
    this.adjustCanvas(window.innerHeight, window.innerWidth);
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.adjustCanvas(event.target.innerHeight, event.target.innerWidth);
  }

  adjustCanvas(height: number, width: number): void {
    if (this.canvas) {
      const el = this.hostElement.nativeElement;
      const rect: DOMRect = el.getBoundingClientRect();
      this.canvas.nativeElement.height = height - rect.y;
      this.canvas.nativeElement.width = width - rect.x;

      console.debug("adjustCanvas", height, width, rect,
        'w', this.canvas.nativeElement.width,
        'h', this.canvas.nativeElement.height);
    }
    this.grawCurvedArrow();
  }

  grawCurvedArrow() {
    const canvas = this.canvas.nativeElement;
    const context: CanvasRenderingContext2D = canvas.getContext('2d');

    context.lineWidth = Math.min(canvas.width, canvas.height) / 7;
    // https://www.w3schools.com/graphics/canvas_gradients.asp
    context.strokeStyle = '#ae2424'; //'#ad2323';
    context.shadowColor = '#343a40'; //'#656565'; //'#f1cbdb';
    context.globalAlpha = 0.7;

    const step = 24;
    const oneX = canvas.width / step; // one step in X
    const oneY = canvas.height / step; // one step in Y
    const startX = oneX * 5;
    const startY = oneY * 8;
    const endX = oneX * 22;
    const endY = oneY * 21;

    // fade to bottom
    //const grd = context.createLinearGradient(endX, startY, endX + oneX * 3, endY + oneY * 3);
    // fade to Top
    // const grd = context.createLinearGradient(endX, endY, endX, startY - oneY * step / 2);
    // https://www.w3schools.com/colors/colors_picker.asp
    // grd.addColorStop(0, '#ae2424');
    // grd.addColorStop(1, '#fbeaea');
    // context.strokeStyle = grd;


    context.lineWidth = Math.min(oneX, oneY) * 3;
    context.shadowBlur = context.lineWidth / 3;

    function animate(current: number) {
      context.clearRect(0, 0, canvas.width, canvas.height);
      context.beginPath();
      // <----
      context.moveTo(endX, startY);
      context.lineTo(startX, startY);

      //   --
      //  |
      //   --
      // https://www.w3schools.com/tags/canvas_beziercurveto.asp
      context.bezierCurveTo(
        startX / 16, startY,
        startX / 16, endY,
        startX, endY);

      // --->
      context.lineTo(endX - oneX * 2, endY);
      //context.stroke();

      // >
      //context.beginPath();
      //context.moveTo(endX - oneX * 2, endY);
      context.lineTo(endX - oneX * 1.99, endY);
      context.lineTo(endX - oneX * 3.5, endY - oneY * 2);
      context.stroke();
      context.closePath();
    }
    animate(1);
  }

  drawCircle() {
    // requestAnimationFrame Shim
    const requestAnimationFrame = window.requestAnimationFrame || window.webkitRequestAnimationFrame;

    const canvas = this.canvas.nativeElement;
    const context = canvas.getContext('2d');
    const x = canvas.width / 2;
    const y = canvas.height / 2;
    const radius = Math.min(x, y) / 2;
    const endPercent = 85;
    const counterClockwise = false;
    const circ = Math.PI * 2;
    const quart = Math.PI / 2;
    let curPerc = 0;

    context.lineWidth = 10;
    context.strokeStyle = '#ad2323';
    context.shadowOffsetX = 0;
    context.shadowOffsetY = 0;
    context.shadowBlur = 15;
    context.shadowColor = '#656565';

    function animate(current: number) {
        context.clearRect(0, 0, canvas.width, canvas.height);
        context.beginPath();
        context.arc(x, y, radius, -(quart), ((circ) * current) - quart, false);
        context.stroke();
        curPerc++;
        if (curPerc < endPercent) {
            requestAnimationFrame(() => {
                animate(curPerc / 100);
            });
        }
    }
    animate(0);
  }
}
