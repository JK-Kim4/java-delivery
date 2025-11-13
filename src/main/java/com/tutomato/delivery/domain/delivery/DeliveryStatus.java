package com.tutomato.delivery.domain.delivery;

public enum DeliveryStatus {

    REQUESTED("배달 요청"),
    ASSIGNED("라이더 배정 완료"),
    IN_DELIVERY("배송 중"),
    COMPLETED("배송 완료"),
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
