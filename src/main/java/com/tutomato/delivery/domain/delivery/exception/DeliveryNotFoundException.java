package com.tutomato.delivery.domain.delivery.exception;

import org.springframework.http.HttpStatus;

public class DeliveryNotFoundException extends RuntimeException {

    private final String message;
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public DeliveryNotFoundException(String message) {
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
