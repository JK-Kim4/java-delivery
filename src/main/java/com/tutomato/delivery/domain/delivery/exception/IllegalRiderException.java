package com.tutomato.delivery.domain.delivery.exception;

import org.springframework.http.HttpStatus;

public class IllegalRiderException extends RuntimeException {

    private final HttpStatus status;

    public IllegalRiderException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public IllegalRiderException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
