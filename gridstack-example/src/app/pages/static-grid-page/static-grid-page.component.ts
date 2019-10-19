import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-static-grid-page',
  templateUrl: './static-grid-page.component.html',
  styleUrls: ['./static-grid-page.component.scss']
})
export class StaticGridPageComponent implements OnInit {

  constructor() { }

  items = [
    { x: 0, y: 0, w: 1, h: 1, name: 'Top Left'},
    { x: 11, y: 0, w: 1, h: 1, name: 'Top Right'},
    { x: 5, y: 5, w: 2, h: 2, name: 'Center'},
    { x: 0, y: 11, w: 12, h: 1, name: 'Footer'}
  ];

  ngOnInit() {}
}
