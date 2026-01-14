import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParcelDetailComponent } from './parcel-detail.component';

describe('ParcelDetailComponent', () => {
  let component: ParcelDetailComponent;
  let fixture: ComponentFixture<ParcelDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParcelDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParcelDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
