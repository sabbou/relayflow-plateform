package com.relayflow.backend.api;

import com.relayflow.backend.service.DuplicateReferenceException;
import com.relayflow.backend.service.ParcelNotFoundException;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ParcelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail NotFound(ParcelNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

    }

    @ExceptionHandler(DuplicateReferenceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail conflict(DuplicateReferenceException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }
}
