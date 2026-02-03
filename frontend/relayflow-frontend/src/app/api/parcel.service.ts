import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { createParcelRequet, Parcel, ParcelStatus } from "./parcel.model";

@Injectable({providedIn: "root"})
export class ParcelService {
private readonly baseUrl = '/api/parcels' ;
constructor(private http :HttpClient) {

}
create (req:createParcelRequet) : Observable<Parcel> {
    return this.http.post<Parcel>(this.baseUrl , req);
}
getByReference(reference :string) :Observable<Parcel> {
    return this.http.get<Parcel>(`${this.baseUrl}/by-reference/${encodeURIComponent(reference)}`);
}
updateStatus(reference : string , status : ParcelStatus) {
return this.http.patch<Parcel> (`${this.baseUrl}/${encodeURIComponent(reference)}/status` , {status})
}
}