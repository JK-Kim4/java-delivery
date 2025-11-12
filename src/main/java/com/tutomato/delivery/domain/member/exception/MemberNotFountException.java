package com.tutomato.delivery.domain.member.exception;

import org.springframework.http.HttpStatus;

public class MemberNotFountException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public MemberNotFountException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
