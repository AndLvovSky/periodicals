package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.exception.BasketItemNotFoundException;
import com.andlvovsky.periodicals.exception.EmptyBasketException;
import com.andlvovsky.periodicals.meta.ClientPages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.view.RedirectView;

@RestControllerAdvice
public class BasketControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BasketItemNotFoundException.class)
    public String handleBasketItemNotFoundException(RuntimeException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(EmptyBasketException.class)
    public RedirectView handleEmptyBasketException() {
        return new RedirectView(ClientPages.BASKET + "?registrationError");
    }

}