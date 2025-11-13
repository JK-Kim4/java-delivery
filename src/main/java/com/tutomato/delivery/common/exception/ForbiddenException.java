package com.tutomato.delivery.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RuntimeException {
    private final String message;
    private HttpStatus httpStatus = HttpStatus.FORBIDDEN;

    public ForbiddenException(String message) {
        this.message = message;
    }

    public ForbiddenException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
