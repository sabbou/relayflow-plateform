import { Routes } from '@angular/router';
import { ParcelCreateComponent } from './parcels/parcel-create/parcel-create.component';
import { ParcelDetailComponent } from './parcels/parcel-detail/parcel-detail.component';

export const routes: Routes = [ { path: 'parcels/new', component: ParcelCreateComponent },
  { path: 'parcels/:reference', component: ParcelDetailComponent },
  { path: '', redirectTo: 'parcels/new', pathMatch: 'full' },
];
