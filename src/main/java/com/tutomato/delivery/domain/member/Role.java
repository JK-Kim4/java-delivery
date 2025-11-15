package com.tutomato.delivery.domain.member;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 역할 유형")
public enum Role {

    @Schema(description = "배달을 수행하는 라이더 계정")
    RIDER,

    @Schema(description = "주문을 접수하고 배달을 요청하는 가게(Store) 계정")
    STORE
}