package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PublicationControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PublicationNotFoundException.class)
    public String handlePublicationNotFound(RuntimeException ex) {
        return ex.getMessage();
    }

}
