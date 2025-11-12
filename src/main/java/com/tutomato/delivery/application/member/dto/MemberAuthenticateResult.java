package com.tutomato.delivery.application.member.dto;

import com.tutomato.delivery.domain.authentication.Tokens;
import com.tutomato.delivery.domain.member.Member;

public record MemberAuthenticateResult(
    Long memberId,
    String account,
    Tokens tokens
) {

    public static MemberAuthenticateResult of(Member member, Tokens tokens) {
        return new MemberAuthenticateResult(member.getId(), member.getAccount(), tokens);
    }
}
