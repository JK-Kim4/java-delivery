package com.tutomato.delivery.domain.member.exception;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends RuntimeException {

    private final HttpStatus status;

    public MemberNotFoundException(String message) {
        this(message, HttpStatus.NOT_FOUND);
    }

    public MemberNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
