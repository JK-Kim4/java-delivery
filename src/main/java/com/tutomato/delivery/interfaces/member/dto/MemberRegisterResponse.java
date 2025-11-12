package com.tutomato.delivery.interfaces.member.dto;

import com.tutomato.delivery.application.member.dto.MemberRegisterResult;
import com.tutomato.delivery.domain.member.Role;
import java.time.LocalDateTime;

public record MemberRegisterResponse(
    Long memberId,
    String id,
    String name,
    Role role,
    LocalDateTime createdAt
) {

    public static MemberRegisterResponse from(
        MemberRegisterResult result
    ) {
        return new MemberRegisterResponse(
            result.memberId(),
            result.account(),
            result.name(),
            result.role(),
            result.createdAt()
        );
    }
}
