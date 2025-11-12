package com.tutomato.delivery.application.member.dto;

import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.domain.member.Role;
import java.time.LocalDateTime;

public record RegisterMemberResult(
    Long memberId,
    String account,
    String name,
    Role role,
    LocalDateTime createdAt
) {

    public static RegisterMemberResult from(Member member) {
        return new RegisterMemberResult(
            member.getId(),
            member.getAccount(),
            member.getName(),
            member.getRole(),
            member.getCreatedAt()
        );
    }
}
