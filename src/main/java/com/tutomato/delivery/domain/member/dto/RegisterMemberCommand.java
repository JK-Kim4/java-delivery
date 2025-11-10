package com.tutomato.delivery.domain.member.dto;

public record RegisterMemberCommand(
    String account,
    String rawPassword,
    String name
) {

    public static RegisterMemberCommand of(
        String account,
        String rawPassword,
        String name
    ) {
        return new RegisterMemberCommand(account, rawPassword, name);
    }
}
