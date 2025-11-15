package com.tutomato.delivery.domain.order.exception;

import org.springframework.http.HttpStatus;

public class IllegalOrderStatusException extends RuntimeException {

    private final String message;
    private final HttpStatus status = HttpStatus.BAD_REQUEST;


    public IllegalOrderStatusException(String message) {
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
