package com.relayflow.backend.api.dto;

import com.relayflow.backend.domain.ParcelStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateParcelStatusRequest( @NotNull ParcelStatus status) {
}
