package com.tutomato.delivery.domain.order;

public enum OrderStatus {

    ORDER_RECEIVED("주문 접수"),
    DELIVERY_STARTED("배송 시작"),
    DELIVERY_COMPLETED("배송 완료"),
    CANCELED("주문 취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canCancel() {
        return this == ORDER_RECEIVED;
    }
}