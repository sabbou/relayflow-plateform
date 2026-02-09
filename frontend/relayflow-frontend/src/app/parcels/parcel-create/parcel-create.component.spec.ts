import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParcelCreateComponent } from './parcel-create.component';
import { ParcelService } from '../../api/parcel.service';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('ParcelCreateComponent', () => {
  let component: ParcelCreateComponent;
  let fixture: ComponentFixture<ParcelCreateComponent>;

  beforeEach(async () => {
    const parcelServiceStub = {
      create: jasmine.createSpy('create'),
    };
 await TestBed.configureTestingModule({
      imports: [ParcelCreateComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(ParcelCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
