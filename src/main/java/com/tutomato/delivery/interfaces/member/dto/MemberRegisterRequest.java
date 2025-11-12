package com.tutomato.delivery.interfaces.member.dto;

import com.tutomato.delivery.application.member.dto.MemberRegisterCommand;

public record MemberRegisterRequest(
    String id,
    String password,
    String name
) {

    public MemberRegisterCommand toCommand() {
        return MemberRegisterCommand.of(id, password, name);
    }

}
