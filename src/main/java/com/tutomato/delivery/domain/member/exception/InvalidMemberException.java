package com.tutomato.delivery.domain.member.exception;

import org.springframework.http.HttpStatus;

public class InvalidMemberException extends RuntimeException {

    private final String message;
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public InvalidMemberException(String message) {
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
