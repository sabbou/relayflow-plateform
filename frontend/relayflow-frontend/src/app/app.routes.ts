import { Routes } from '@angular/router';
import { ParcelCreateComponent } from './parcels/parcel-create/parcel-create.component';

export const routes: Routes = [ { path: 'parcels/new', component: ParcelCreateComponent },
  { path: '', redirectTo: 'parcels/new', pathMatch: 'full' },
];
