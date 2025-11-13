package com.tutomato.delivery.domain.delivery.exception;

import org.springframework.http.HttpStatus;

public class IllegalRiderException extends RuntimeException {

    private final String message;
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public IllegalRiderException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {

        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
