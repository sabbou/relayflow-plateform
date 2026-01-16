package com.relayflow.backend.domain;

import com.relayflow.backend.service.InvalidStatusTransitionException;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "parcels")
public class Parcel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ParcelStatus status = ParcelStatus.CREATED;

    @Column(name = "created_at",nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at",nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public ParcelStatus getStatus() { return status; }
    public void setStatus(ParcelStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public static Parcel create(String reference) {
        Parcel p = new Parcel();
        p.reference = reference;           // si tu peux (sinon via setter)
        p.status = ParcelStatus.CREATED;
        p.createdAt = Instant.now();
        p.updatedAt = Instant.now();
        return p;
    }
    public void changeStatus (ParcelStatus newStatus) {
        if(this.status==ParcelStatus.DELIVERED) {
            throw new InvalidStatusTransitionException(this.status, newStatus);
        }
      if(!isNextStatus(newStatus)) {
          throw new InvalidStatusTransitionException(this.status, newStatus);
      }
      this.status = newStatus;
      this.updatedAt = Instant.now();
    }
    private boolean isNextStatus(ParcelStatus newStatus) {
        return switch (this.status){
            case CREATED -> newStatus.equals(ParcelStatus.IN_TRANSIT);
            case IN_TRANSIT -> newStatus.equals(ParcelStatus.ARRIVED_AT_RELAY);
            case ARRIVED_AT_RELAY -> newStatus.equals(ParcelStatus.DELIVERED);
            case DELIVERED -> false;

        };
    }
}

