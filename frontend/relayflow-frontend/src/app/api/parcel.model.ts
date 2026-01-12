
export type ParcelStatus ='CREATED' |'IN_TRANSIT' | 'ARRIVED_AT_RELAY' | 'DELIVERED'
export interface Parcel {
    id : String ;
    reference : String ;
    status  :ParcelStatus;
    createdAt? : String ;
    updateAt? : String;
}
export interface createParcelRequet {
reference : string ;
}
