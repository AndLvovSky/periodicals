package com.andlvovsky.periodicals.controller.exception;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PublicationControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PublicationNotFoundException.class)
    public String handlePublicationNotFoundException(RuntimeException ex) {
        return ex.getMessage();
    }

}
