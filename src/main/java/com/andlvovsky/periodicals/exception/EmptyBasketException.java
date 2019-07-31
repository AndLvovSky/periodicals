package com.andlvovsky.periodicals.exception;

public class EmptyBasketException extends RuntimeException {

    public EmptyBasketException() {
        super("Basket cannot be empty");
    }

}
