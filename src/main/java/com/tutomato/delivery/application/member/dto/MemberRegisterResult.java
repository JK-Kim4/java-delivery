package com.tutomato.delivery.application.member.dto;

import com.tutomato.delivery.domain.member.Member;
import com.tutomato.delivery.domain.member.Role;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record MemberRegisterResult(
        Long memberId,
        String account,
        String name,
        Role role,
        LocalDateTime createdAt
) {

    public static MemberRegisterResult from(Member member) {
        return new MemberRegisterResult(
                member.getId(),
                member.getAccount(),
                member.getName(),
                member.getRole(),
                LocalDateTime.ofInstant(member.getCreatedAt(), ZoneId.of("Asia/Seoul"))
        );
    }
}
