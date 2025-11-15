package com.tutomato.delivery.domain.delivery.exception;

import org.springframework.http.HttpStatus;

public class IllegalDeliveryStatusException extends RuntimeException {

    private final HttpStatus httpStatus;

    public IllegalDeliveryStatusException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public IllegalDeliveryStatusException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
