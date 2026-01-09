package com.relayflow.backend.service;

public class DuplicateReferenceException extends RuntimeException {
    public DuplicateReferenceException(String reference) {
        super("Reference already exists: " + reference);
    }
}
