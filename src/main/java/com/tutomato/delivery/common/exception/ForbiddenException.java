package com.tutomato.delivery.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ForbiddenException(String message) {
        this(message, HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
