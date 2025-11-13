package com.tutomato.delivery.application.delivery.dto;

public record DeliveryStatusUpdateCommand(
    Long deliveryId,
    Long riderMemberId,
    Long storeMemberId
) {

    public static DeliveryStatusUpdateCommand createWithRiderId(Long deliveryId, Long riderMemberId) {
        return new DeliveryStatusUpdateCommand(deliveryId, riderMemberId, null);
    }

    public static DeliveryStatusUpdateCommand createWithStoreId(Long deliveryId, Long storeMemberId) {
        return new DeliveryStatusUpdateCommand(deliveryId, null, storeMemberId);
    }
}
