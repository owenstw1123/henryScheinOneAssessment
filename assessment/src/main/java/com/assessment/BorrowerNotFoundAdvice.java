package com.assessment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class BorrowerNotFoundAdvice {

    @ExceptionHandler(BorrowerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String borrowerNotFoundHandler(BorrowerNotFoundException ex) {
        return ex.getMessage();
    }
}
