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
    this.draw();
  }

  draw() {
    // requestAnimationFrame Shim
    const requestAnimationFrame = window.requestAnimationFrame || window.webkitRequestAnimationFrame;

    const canvas = this.canvas.nativeElement;
    const context = canvas.getContext('2d');
    const x = canvas.width / 2;
    const y = canvas.height / 2;
    const radius = (x + y) / 2 / 2;
    const endPercent = 85;
    let curPerc = 0;
    const counterClockwise = false;
    const circ = Math.PI * 2;
    const quart = Math.PI / 2;

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
