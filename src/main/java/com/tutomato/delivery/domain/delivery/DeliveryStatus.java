package com.tutomato.delivery.domain.delivery;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배달 상태 코드")
public enum DeliveryStatus {

    @Schema(description = "배달 요청 접수 상태 (아직 라이더 미배정)")
    REQUESTED("배달 요청"),

    @Schema(description = "라이더 배정 완료 상태 (픽업 대기)")
    ASSIGNED("라이더 배정 완료"),

    @Schema(description = "라이더가 픽업을 완료하고 배송 중인 상태")
    IN_DELIVERY("배송 중"),

    @Schema(description = "배송이 완료된 상태")
    COMPLETED("배송 완료"),

    @Schema(description = "주문/배달이 취소된 상태")
    CANCELED("배달 취소");


    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // 배달 상태 변경 가능 여부 확인
    public boolean isDestinationChangeAllowed() {
        return this == REQUESTED || this == ASSIGNED;
    }
}
