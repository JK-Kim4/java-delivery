package com.tutomato.delivery.domain.authentication.exception;

public class UnAuthorizedException extends RuntimeException {
    private final AuthErrorCode authErrorCode;

    public UnAuthorizedException(AuthErrorCode authErrorCode) {
        this.authErrorCode = authErrorCode;
    }

}
