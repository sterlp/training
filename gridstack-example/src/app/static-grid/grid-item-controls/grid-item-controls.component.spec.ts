import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GridItemControlsComponent } from './grid-item-controls.component';

describe('GridItemControlsComponent', () => {
  let component: GridItemControlsComponent;
  let fixture: ComponentFixture<GridItemControlsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GridItemControlsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GridItemControlsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
