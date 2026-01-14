package com.relayflow.backend.service;

import java.util.UUID;

public class ParcelNotFoundException extends RuntimeException {
    public ParcelNotFoundException(String reference) {
        super("Could not find parcel with reference: " + reference);
    }
}
