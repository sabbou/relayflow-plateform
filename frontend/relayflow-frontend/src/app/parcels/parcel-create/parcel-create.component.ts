import { Component } from '@angular/core';
import { Parcel } from '../../api/parcel.model';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ParcelService } from '../../api/parcel.service';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-parcel-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './parcel-create.component.html',
  styleUrl: './parcel-create.component.scss'
})
export class ParcelCreateComponent {
created?: Parcel;
  errorMessage?: string;
  isLoading = false;
  form;  

    constructor(private fb: FormBuilder, private parcelService: ParcelService) {
      this.form = this.fb.group({
      reference: ['', [Validators.required, Validators.maxLength(50)]],
    });
  }


 submit () :void {
this.errorMessage =undefined ;
this.created =undefined ;
if(this.form.invalid){
  this.form.markAllAsTouched();
  return;
}
const reference=(this.form.value.reference ?? '').trim();
this.isLoading=true;
this.parcelService.create({reference}).subscribe({
  next:(p)=>{
this.created=p;
this.isLoading=false ;
this.form.reset()
  },
  error :(err : HttpErrorResponse)=>{
    this.isLoading=false ;
    if(err.status===409){
      this.errorMessage='Cette référence existe déjà. Merci de choisir une autre.'
    return ;
    }
      this.errorMessage = 'Erreur serveur ou réseau. Vérifie que le backend est démarré.'
  },
});
}
 get referenceCtrl() {
    return this.form.controls.reference;
  }
}
