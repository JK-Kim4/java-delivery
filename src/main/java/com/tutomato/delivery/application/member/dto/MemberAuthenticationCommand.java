package com.tutomato.delivery.application.member.dto;

public record MemberAuthenticationCommand(
    String account,
    String rawPassword
) {

}
