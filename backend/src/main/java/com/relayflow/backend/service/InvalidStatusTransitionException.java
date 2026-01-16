package com.relayflow.backend.service;

import com.relayflow.backend.domain.ParcelStatus;

public class InvalidStatusTransitionException extends RuntimeException{
    public InvalidStatusTransitionException(ParcelStatus from, ParcelStatus to) {
        super("Transition invalide de " + from + " to " + to);
    }
}
