import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-canvas-grid-page',
  templateUrl: './canvas-grid-page.component.html',
  styleUrls: ['./canvas-grid-page.component.scss']
})
export class CanvasGridPageComponent implements OnInit {

  constructor() { }
  items = [
    { x: 20, y: 3, w: 8, h: 5, name: '#1'},
    { x: 10, y: 3, w: 8, h: 5, name: '#2'},
    { x: 2,  y: 10, w: 8, h: 5, name: '#3'},

    { x: 5,  y: 16, w: 8, h: 5, name: '#3'},
    { x: 15,  y: 16, w: 8, h: 5, name: '#3'},
  ];
  ngOnInit() {
  }
}
