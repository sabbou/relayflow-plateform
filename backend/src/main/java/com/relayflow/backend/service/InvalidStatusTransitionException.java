package com.relayflow.backend.service;

import com.relayflow.backend.domain.ParcelStatus;

public class InvalidStatusTransitionException extends RuntimeException{
    private final ParcelStatus from;
    private final ParcelStatus to;
    public InvalidStatusTransitionException(ParcelStatus from, ParcelStatus to) {
        super("Transition invalide de " + from + " to " + to);
        this.from = from;
        this.to = to;
    }

    public ParcelStatus getTo() {
        return to;
    }

    public ParcelStatus getFrom() {
        return from;
    }
}
