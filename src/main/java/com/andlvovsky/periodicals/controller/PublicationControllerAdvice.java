package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PublicationControllerAdvice {

    @ExceptionHandler(PublicationNotFoundException.class)
    public String handlePublicationNotFound(RuntimeException ex) {
        return "<h2 style=\"color:red;\">" + ex.getMessage() + "</h2>";
    }

}
