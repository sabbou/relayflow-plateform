
export type ParcelStatus ='CREATED' |'IN_TRANSIT' | 'ARRIVED_AT_RELAY' | 'DELIVERED'
export interface Parcel {
    id : string;
    reference : string ;
    status  :ParcelStatus;
    createdAt : string ;
    updateAt : string;
}
export interface createParcelRequet {
reference : string ;
}
