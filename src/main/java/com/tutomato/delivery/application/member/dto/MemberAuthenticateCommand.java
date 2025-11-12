package com.tutomato.delivery.application.member.dto;

public record MemberAuthenticateCommand(
    String account,
    String rawPassword
) {

    public static MemberAuthenticateCommand of(String account, String rawPassword) {
        return new MemberAuthenticateCommand(account, rawPassword);
    }

}
