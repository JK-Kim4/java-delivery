package com.tutomato.delivery.domain.authentication.exception;

public class UnAuthorizedException extends RuntimeException {
    private final AuthErrorCode authErrorCode;

    public UnAuthorizedException(AuthErrorCode authErrorCode) {
        super(authErrorCode.getMessage());   // 예외 message 설정
        this.authErrorCode = authErrorCode;
    }

    public AuthErrorCode getAuthErrorCode() {
        return authErrorCode;
    }

}
