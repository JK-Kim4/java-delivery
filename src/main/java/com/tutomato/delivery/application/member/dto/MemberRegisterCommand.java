package com.tutomato.delivery.application.member.dto;

import com.tutomato.delivery.domain.member.Role;

public record MemberRegisterCommand(
    String account,
    String rawPassword,
    String name,
    Role role
) {

    public static MemberRegisterCommand of(
        String account,
        String rawPassword,
        String name,
        Role role
    ) {
        return new MemberRegisterCommand(account, rawPassword, name, role);
    }
}
