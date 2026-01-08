package com.relayflow.backend.repository;

import com.relayflow.backend.domain.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParcelRepository extends JpaRepository<Parcel, UUID> {
    Optional<Parcel> findByReference(String reference);
    boolean existsByReference(String reference);
}
