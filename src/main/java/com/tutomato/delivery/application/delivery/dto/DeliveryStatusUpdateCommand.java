package com.tutomato.delivery.application.delivery.dto;

import com.tutomato.delivery.common.extractor.AuthMemberRequest;

public record DeliveryStatusUpdateCommand(
    Long deliveryId,
    Long riderMemberId,
    Long storeMemberId
) {

    public static DeliveryStatusUpdateCommand createWithRiderId(Long deliveryId, Long riderId) {
        return new DeliveryStatusUpdateCommand(deliveryId, riderId, null);
    }

    public static DeliveryStatusUpdateCommand createWithStoreId(Long deliveryId, Long storeId) {
        return new DeliveryStatusUpdateCommand(deliveryId, null, storeId);
    }

    public static DeliveryStatusUpdateCommand createWithRider(Long deliveryId, AuthMemberRequest authMember) {
        return new DeliveryStatusUpdateCommand(deliveryId, authMember.id(), null);
    }

    public static DeliveryStatusUpdateCommand createWithStore(Long deliveryId, AuthMemberRequest authMember) {
        return new DeliveryStatusUpdateCommand(deliveryId, null, authMember.id());
    }
}
