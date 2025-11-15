package com.tutomato.delivery.domain.authentication.exception;

import org.springframework.http.HttpStatus;

public enum AuthErrorCode {

    AUTH_MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "인증 토큰이 필요합니다."),
    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 토큰입니다."),
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 인증 토큰입니다."),
    AUTH_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "토큰 서명이 올바르지 않습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    AuthErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
