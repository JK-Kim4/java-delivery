package com.tutomato.delivery.domain.member.exception;

import org.springframework.http.HttpStatus;

public class InvalidMemberException extends RuntimeException {

    private final HttpStatus status;

    public InvalidMemberException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public InvalidMemberException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
