package com.tutomato.delivery.interfaces.member.dto;

import com.tutomato.delivery.application.member.dto.MemberRegisterCommand;
import com.tutomato.delivery.domain.member.Role;

public record MemberRegisterRequest(
    String id,
    String password,
    String name,
    Role role
) {

    public MemberRegisterCommand toCommand() {
        return MemberRegisterCommand.of(id, password, name, role);
    }

}
