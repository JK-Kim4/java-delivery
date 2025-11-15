package com.tutomato.delivery.domain.member.exception;

import org.springframework.http.HttpStatus;

public class IllegalMemberRoleException extends RuntimeException {

    private final HttpStatus status;

    public IllegalMemberRoleException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public IllegalMemberRoleException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
