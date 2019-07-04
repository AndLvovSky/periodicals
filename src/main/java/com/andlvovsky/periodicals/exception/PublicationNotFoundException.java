package com.andlvovsky.periodicals.exception;

public class PublicationNotFoundException extends RuntimeException {

    public PublicationNotFoundException(Long id) {
        super("cannot find publication with id: " + id);
    }

}
