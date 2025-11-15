package com.tutomato.delivery.domain.delivery.exception;

import org.springframework.http.HttpStatus;

public class DeliveryNotFoundException extends RuntimeException {

    private final HttpStatus status;

    public DeliveryNotFoundException(String message) {
        this(message, HttpStatus.NOT_FOUND);
    }

    public DeliveryNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
