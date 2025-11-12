package com.tutomato.delivery.interfaces.member.dto;

import com.tutomato.delivery.application.member.dto.MemberAuthenticateResult;
import com.tutomato.delivery.domain.authentication.Tokens;

public record MemberAuthenticateResponse(
    Long memberId,
    String account,
    Tokens tokens
) {

    public static MemberAuthenticateResponse from(MemberAuthenticateResult result) {
        return new MemberAuthenticateResponse(result.memberId(), result.account(), result.tokens());
    }

}
