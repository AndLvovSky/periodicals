package com.andlvovsky.periodicals.exception;

public class BasketItemNotFoundException extends RuntimeException {

    public BasketItemNotFoundException(int index) {
        super("cannot find basket item at index: " + index);
    }

}
