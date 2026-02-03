import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ParcelService } from '../../api/parcel.service';
import { BehaviorSubject, catchError, combineLatest, map, of, switchMap } from 'rxjs';
import { Parcel, ParcelStatus } from '../../api/parcel.model';
import { AsyncPipe, DatePipe, NgIf } from '@angular/common';

//ViewState = le contrat / la structure de données , Ce n’est pas une variable,Ce n’est pas un observable :juste un type TypeScript,décrit toutes les formes possibles de l’état de la page
type ViewState = |{state :'loading'}
|{state :'error' ; message : string}
|{state :'notfound'}
|{state :'ready'; parcel :Parcel ,isUpdating : boolean ; updateError ?:string}

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
private updating$ = new BehaviorSubject<boolean>(false); //état de mise à jour
private updateError$ = new BehaviorSubject <String | null >(null);//erreur éventuelle
//le choix de behaviviorsubject
// behaviviorsubject parce que ça devient réactif qui peut être observée , émettre des nouvelles
//  valeurs ou bien garder la dernière  valeur courante (si on choisit subject : le subject n'a pas de valeur initiale mais behaviorsubject a tujours une valeur acttuelle)
//avant vm$ représente est l'unique état principalede la page 
//  il émettatit directement les loading / error / notfound / ready parce 
// que c'est un observable viewstate maintenant on change l'architecture :
//reference$ : Observable<string | null> (juste la valeur d’URL) 
// // le choix d'observable car La valeur reference peut changer sans que le composant soit détruit
//parcel$ : Observable<ViewState> (loading/error/notfound/ready)
//updating$ : Observable<boolean> (état “je suis en train de PATCH”)
//updateError$ : Observable<string | null> (erreur du PATCH)
//vm$ :un observable qui émet un tableau avec les 3 dernières valeurs :
// en d'autre termes Je construis un ViewModel unique à partir 
// de 3 sources d’état réactives: (État du chargement du colis , État de mise à jour en cours ,État d’erreur de mise à jour en utilisant combinelast
// À chaque changement, l’écran se met à jour automatiquement.: autrement : écran = combine(
  //étatDuColis,
  //estEnTrainDeMettreAJour,
  //erreurDeMiseAJour
//).map(les3États => construireUnObjetPourLUI)
//Reactive ViewModel Composition C’est exactement ce qu’on fait en Angular moderne / React / Vue
//vm$l’état réel et vivant de la page
//pour info combinelast utilise un tableau parce rxjs émet les valeurs sous forme un tableau

private reference$=this.route.paramMap.pipe(map(pm=>pm.get('reference')),);
/*vm$=this.route.paramMap.pipe(map(pm=>pm.get('reference')),
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
);*/
private parcel$ =this.reference$.pipe(switchMap(reference=>{
  if(!reference) return of<ViewState>({state : 'error' , message : 'reference manquante dans l URL'});
return this.parcelService.getByReference(reference).pipe(
  map(parcel=>({state : 'ready' , parcel , isUpdating :false} as ViewState)),
  catchError(err=>{
    if(err?.status===404) return of <ViewState>({state : 'notfound'});
    return of <ViewState>({state : 'error' , message :'Erreur lors du chargement du colis'}) ;
  })
);
})
);
//{ ...vm } = copie de l’objet : si vm est ts : {state : 'ready' , parcel : {...}} Alors{..vm} donne : ts {state : 'ready' , parcel : {...}}
vm$= combineLatest([this.parcel$ ,this.updating$ , this.updateError$ ]).pipe(map(([vm,isUpdating , updateError])=>{
  if(vm.state !== 'ready') return vm;
  return { ...vm , isUpdating, updateError :updateError ?? undefined} as ViewState ;
}
)) ;
nextAction(status : ParcelStatus) : {label : String ; next : ParcelStatus} | null {
  switch(status){
    case 'CREATED' :
      return {label : 'Expédier' , next : 'IN_TRANSIT'};
      case 'IN_TRANSIT' :
        return {label : 'Arrivé au relais' , next : 'ARRIVED_AT_RELAY'}
        case 'ARRIVED_AT_RELAY' :
          return {label : 'Livré' , next : 'DELIVERED'};
          case 'DELIVERED' :
            return null;
  }
}
updateStatus (reference : string , next : ParcelStatus){
  this.updateError$.next(null);
  this.updating$.next(true);
  this.parcelService.updateStatus (reference , next).subscribe({
    //// petit hack simple : on force un reload via navigation ou on peut mieux faire avec refresh stream
        // Ici : on recharge la page en relançant le fetch (re-souscription) en resetant updating.
        next: () => {
          this.updating$.next(false);
          // Pour refléter immédiatement sans complexifier, tu peux aussi faire un "soft reload" :
        // => le plus simple est de rafraîchir en rechargant la route (si tu veux je te donne la version propre).
        location.reload();
        },
        error: (err)=> {
          this.updating$.next(false);
          if(err?.status===400){
            this.updateError$.next(err?.error?.message?? 'Transition invalide');
          }
          else if(err?.status===404) {
            this.updateError$.next('Colis introuvable');
          }else {
            this.updateError$.next('Erreur lors de la mise à jour du statut');
          }
        }
  });
}
}

