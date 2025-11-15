package com.tutomato.delivery.domain.delivery.exception;

import org.springframework.http.HttpStatus;

public class IllegalDeliveryStatusException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public IllegalDeliveryStatusException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
