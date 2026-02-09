import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { ParcelDetailComponent } from './parcel-detail.component';
import { ParcelService } from '../../api/parcel.service';
import { of, Subject, throwError } from 'rxjs';
import { Parcel, ParcelStatus } from '../../api/parcel.model';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

describe('ParcelDetailComponent', () => {
  let component: ParcelDetailComponent;
  let fixture: ComponentFixture<ParcelDetailComponent>;
  let parcelService : jasmine.SpyObj<ParcelService>;
  let paramMap$ = new Subject<any>();

  const parcelCreated : Parcel= {

    id:'1',
    reference :'REF-001',
    status : 'CREATED',
    createdAt : '2026-01-01T10:00:00Z',
    updatedAt: null as any

  };


  beforeEach(async () => {

parcelService =jasmine.createSpyObj<ParcelService>('ParcelService',[
  'getByReference',
  'updateStatus'

]);

await TestBed.configureTestingModule({
      imports: [ParcelDetailComponent],
      providers : [
        {provide : ParcelService, useValue : parcelService},
        {
          provide :ActivatedRoute,
          useValue : {
            paramMap:paramMap$.asObservable()
        }
        }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ParcelDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  function emitReference(ref: string | null){
    paramMap$.next(convertToParamMap(ref ?{reference :ref}:{}));
  }
  it('should show error when reference missing',  fakeAsync(() => {
    parcelService.getByReference.and.returnValue(of(parcelCreated));// not used
    //const fixture = TestBed.createComponent(ParcelDetailComponent);
    //fixture.detectChanges();
    emitReference(null);
    tick();
    fixture.detectChanges();
    //const html =fixture.nativeElement as HTMLElement;

    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    //expect(html.textContent).toContain('reference'); //"reference manquante"
    expect(text.toLowerCase()).toContain('reference');
  }));

   it('should show go to ready when get succeeds', fakeAsync(() => {
        const parcel = { ...parcelCreated, reference: 'REF-021' };
//parcelService.getByReference.and.returnValue(of(parcelCreated));
    //const fixture = TestBed.createComponent(ParcelDetailComponent);
    //fixture.detectChanges();
     parcelService.getByReference.and.callFake((ref: string) =>
    of({ ...parcelCreated, reference: ref })
  );
    emitReference('REF-021');
    tick();
    fixture.detectChanges();
    //const html =fixture.nativeElement as HTMLElement;const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
 /*expect(html.textContent).toContain('Détails colis'); 
    expect(html.textContent).toContain('REF-021');
    expect(html.textContent).toContain('CREATED');*/
    expect(text).toContain('Détail colis');
    expect(text).toContain('REF-021');
    expect(text).toContain('CREATED');
  }));

     it('should show go not found when get return 404',  fakeAsync(() => {
    parcelService.getByReference.and.returnValue(throwError(()=>({status :404})));
    //const fixture = TestBed.createComponent(ParcelDetailComponent);
    //fixture.detectChanges();
    emitReference('REF-022');
      //fixture.detectChanges();
      tick();
      fixture.detectChanges();
    /*const html =fixture.nativeElement as HTMLElement;
    expect(html.textContent).toContain('Colis introuvable'); */
    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(text).toContain('Colis introuvable');
   
  }));

      it('should show error not found when get fails 500', fakeAsync(() => {
    parcelService.getByReference.and.returnValue(throwError(()=>({status :500})));
    //const fixture = TestBed.createComponent(ParcelDetailComponent);
    //fixture.detectChanges();
    emitReference('REF-023');
    tick();
    fixture.detectChanges();
    /*const html =fixture.nativeElement as HTMLElement;
    expect(html.textContent).toContain('Erreur lors du chargement'); */
    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(text).toContain('Erreur lors du chargement du colis');
   
  }));

   it('should display correct action button for CREATED',fakeAsync(() => {
     parcelService.getByReference.and.returnValue(of(parcelCreated));
    //const fixture = TestBed.createComponent(ParcelDetailComponent);
    //fixture.detectChanges();
    emitReference('REF-024');
    tick();
    fixture.detectChanges();
    const btn = fixture.nativeElement.querySelector('button') as HTMLButtonElement;
    expect(btn).toBeTruthy();
    expect(btn.textContent?.trim()).toContain('Expédier');
   }));


    it('should call updateStatus with next status on click', fakeAsync(() => {
     parcelService.getByReference.and.returnValue(of(parcelCreated));
         parcelService.updateStatus.and.returnValue(of(parcelCreated)); //succès
    //const fixture = TestBed.createComponent(ParcelDetailComponent);
    //fixture.detectChanges();
    emitReference('REF-025');
    tick();
    fixture.detectChanges();
    const btn = fixture.nativeElement.querySelector('button') as HTMLButtonElement;
   btn.click();
   tick();
   fixture.detectChanges();
    expect(parcelService.updateStatus).toHaveBeenCalledWith('REF-001', 'IN_TRANSIT' as ParcelStatus);
   
  }));

  
  it('should show 400 error meassage on Patch failure',fakeAsync(()=>{
    parcelService.getByReference.and.returnValue(of(parcelCreated));
     parcelService.updateStatus.and.returnValue(throwError(()=>({status: 400 , error : {detail : 'Transition invalide'}})));
  //const fixture = TestBed.createComponent(ParcelDetailComponent);
    //fixture.detectChanges();
    emitReference('REF-026');
    tick();
    fixture.detectChanges();
    const btn = fixture.nativeElement.querySelector('button') as HTMLButtonElement;
    btn.click();
    tick();
    fixture.detectChanges();
    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(text).toContain('Transition invalide');
    /*const html = fixture.nativeElement as HTMLElement;
    expect(html.textContent).toContain('Transition');*/
    }));

     it('should show 404 error meassage on Patch failure',fakeAsync(()=>{
    parcelService.getByReference.and.returnValue(of(parcelCreated));
     parcelService.updateStatus
     .and.returnValue(throwError(()=>({status: 404})));
  
     //const fixture = TestBed.createComponent(ParcelDetailComponent);
    //fixture.detectChanges();
    emitReference('REF-027');
    tick();
    fixture.detectChanges();
    const btn = fixture.nativeElement.querySelector('button') as HTMLButtonElement;
    btn.click();
    tick();
    fixture.detectChanges();
    const text = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(text).toContain('Colis introuvable');
  }));

});
