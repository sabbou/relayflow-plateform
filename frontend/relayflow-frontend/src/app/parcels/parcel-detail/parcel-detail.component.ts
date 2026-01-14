import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ParcelService } from '../../api/parcel.service';
import { catchError, map, of, switchMap } from 'rxjs';
import { Parcel } from '../../api/parcel.model';
import { AsyncPipe, DatePipe, NgIf } from '@angular/common';

type ViewState = |{state :'loading'}
|{state :'error' ; message : string}
|{state :'notfound'}
|{state :'ready'; parcel :Parcel}

@Component({
  selector: 'app-parcel-detail',
  standalone: true,
  imports: [DatePipe , NgIf , AsyncPipe,RouterLink],
  templateUrl: './parcel-detail.component.html',
  styleUrl: './parcel-detail.component.scss'
})
export class ParcelDetailComponent {
private route =inject(ActivatedRoute);
private parcelService = inject(ParcelService);
vm$=this.route.paramMap.pipe(map(pm=>pm.get('reference')),
switchMap (reference=>{
  if(!reference) return of<ViewState>({state :'error', message : 'reference manquant dans l URL'});
  return this.parcelService.getByReference (reference).pipe(
    map(parcel=>({state : 'ready' ,parcel}) as ViewState),
catchError(err=>{
  if (err?.status===404) return of <ViewState>({state :'notfound'});
return of<ViewState>({state :'error', message : 'erreur lors du chargement du colis'});
})
);
}),
);

}
