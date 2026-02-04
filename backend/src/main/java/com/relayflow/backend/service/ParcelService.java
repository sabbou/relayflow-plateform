package com.relayflow.backend.service;

import com.relayflow.backend.api.dto.CreateParcelRequest;
import com.relayflow.backend.api.dto.ParcelResponse;
import com.relayflow.backend.domain.Parcel;
import com.relayflow.backend.domain.ParcelStatus;
import com.relayflow.backend.repository.ParcelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ParcelService {
    private final ParcelRepository parcelRepository;
    public ParcelService(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

 /*public ParcelResponse getById(UUID id) {
        Parcel parcel = parcelRepository.findById(id).orElseThrow(()-> new ParcelNotFoundException(id));
        return toResponse(parcel);
    }*/
     public ParcelResponse getByReference(String reference) {
        Parcel parcel = parcelRepository.findByReference(reference).orElseThrow(()-> new ParcelNotFoundException(reference));
        return toResponse(parcel);
    }


    public ParcelResponse create(CreateParcelRequest parcelRequest)
    {
        String ref=parcelRequest.reference().trim();
        if (parcelRepository.existsByReference(ref)) {
            throw new DuplicateReferenceException(ref);
        }
        Parcel parcel = Parcel.create(ref);
        //parcel.setReference(ref);//Évite le double. Si Parcel.create(ref) met déjà la référence, je ne dois pas faire setReference(ref) après.
       Parcel savedParcel=  parcelRepository.save(parcel);
        return toResponse(savedParcel);
    }
    private ParcelResponse toResponse(Parcel p) {
        return new ParcelResponse(
                p.getId(),
                p.getReference(),
                p.getStatus(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );


    }
@Transactional
public ParcelResponse changeStatus(String reference, ParcelStatus status)
{
    Parcel parcel = parcelRepository.findByReference(reference).orElseThrow(()-> new ParcelNotFoundException(reference));
    parcel.changeStatus(status);
   // return parcelRepository.save(parcel); hibernate va flusher tout seul parce qu'on a @transactional
    return toResponse(parcel) ;
}
}
