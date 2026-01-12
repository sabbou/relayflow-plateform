import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParcelCreateComponent } from './parcel-create.component';

describe('ParcelCreateComponent', () => {
  let component: ParcelCreateComponent;
  let fixture: ComponentFixture<ParcelCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParcelCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParcelCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
