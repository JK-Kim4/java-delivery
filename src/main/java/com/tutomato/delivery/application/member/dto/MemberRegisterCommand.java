package com.tutomato.delivery.application.member.dto;

public record MemberRegisterCommand(
    String account,
    String rawPassword,
    String name
) {

    public static MemberRegisterCommand of(
        String account,
        String rawPassword,
        String name
    ) {
        return new MemberRegisterCommand(account, rawPassword, name);
    }
}
