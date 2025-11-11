package com.tutomato.delivery.application.member.dto;

import com.tutomato.delivery.domain.authentication.Tokens;

public record MemberAuthenticationResult(
    Tokens tokens
) {

    public static MemberAuthenticationResult from(Tokens tokens) {
        return new MemberAuthenticationResult(tokens);
    }
}
