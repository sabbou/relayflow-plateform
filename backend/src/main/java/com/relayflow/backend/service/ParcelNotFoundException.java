package com.relayflow.backend.service;

import java.util.UUID;

public class ParcelNotFoundException extends RuntimeException {
    public ParcelNotFoundException(UUID id) {
        super("Could not find parcel with id: " + id);
    }
}
