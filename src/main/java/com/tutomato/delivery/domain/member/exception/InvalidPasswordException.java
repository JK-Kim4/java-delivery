package com.tutomato.delivery.domain.member.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends RuntimeException {

    private final HttpStatus status;

    public InvalidPasswordException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public InvalidPasswordException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
