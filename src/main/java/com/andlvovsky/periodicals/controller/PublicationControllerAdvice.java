package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class PublicationControllerAdvice {

    @ResponseBody
    @ExceptionHandler(PublicationNotFoundException.class)
    public String handlePublicationNotFound(PublicationNotFoundException ex) {
        return "<h2 style=\"color:red;\">" + ex.getMessage() + "</h2>";
    }

}
