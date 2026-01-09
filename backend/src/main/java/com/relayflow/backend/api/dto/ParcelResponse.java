package com.relayflow.backend.api.dto;

import com.relayflow.backend.domain.ParcelStatus;

import java.time.Instant;
import java.util.UUID;

public record ParcelResponse(UUID id,
                             String reference,
                             ParcelStatus status,
                             Instant createdAt,
                             Instant updatedAt) {
}
