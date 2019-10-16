import { Component, OnInit } from '@angular/core';
declare var $: any; 

/**
 * https://github.com/gridstack/gridstack.js
 * https://www.npmjs.com/package/gridstack
 * http://gridstackjs.com/demo/
 * https://github.com/gridstack/gridstack.js/tree/develop/demo
 * 
 * https://bl.ocks.org/wheresjames/3689d262f2a0edf98eee0b6ad781e511
 */
@Component({
  selector: 'app-grid-stack-page',
  templateUrl: './grid-stack-page.component.html',
  styleUrls: ['./grid-stack-page.component.scss']
})
export class GridStackPageComponent implements OnInit {

  constructor() {
    $(function () {
      var options = {
          height: 12,
          width: 12,
          float: true,
          disableOneColumnMode: true,
          alwaysShowResizeHandle: /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)
      };
      $('.grid-stack').gridstack(options);
    });

  }

  ngOnInit() {
  }

}
