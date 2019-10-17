import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CanvasGridPageComponent } from './canvas-grid-page.component';

describe('CanvasGridPageComponent', () => {
  let component: CanvasGridPageComponent;
  let fixture: ComponentFixture<CanvasGridPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CanvasGridPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CanvasGridPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
