package com.relayflow.backend.api;

import com.relayflow.backend.service.DuplicateReferenceException;
import com.relayflow.backend.service.InvalidStatusTransitionException;
import com.relayflow.backend.service.ParcelNotFoundException;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

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

//    @ExceptionHandler(InvalidStatusTransitionException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ProblemDetail handleInvalidStatus(InvalidStatusTransitionException ex) {
//        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
//    }
    @ExceptionHandler(InvalidStatusTransitionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleInvalidStatus(InvalidStatusTransitionException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Invalid parcel status transition");
        pd.setProperty("code", "PARCEL_INVALID_TRANSITION");
        pd.setProperty("from", ex.getFrom());
        pd.setProperty("to", ex.getTo());
        return pd;
    }
    //Ça donne au front :
    //detail : message lisible
    //code : stable pour gérer l’affichage
    //from/to : debug / UI éventuelle
}
