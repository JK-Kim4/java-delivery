package com.tutomato.delivery.interfaces.member.dto;

import com.tutomato.delivery.application.member.dto.MemberAuthenticateCommand;

public record MemberAuthenticateRequest(
    String id,
    String password
) {

    public MemberAuthenticateCommand toCommand() {
        return MemberAuthenticateCommand.of(id, password);
    }
}
