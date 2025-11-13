package com.tutomato.delivery.application.delivery.dto;

import com.tutomato.delivery.domain.delivery.Delivery;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record DeliveryStatusUpdateResult(
    Long riderMemberId,
    Long deliveryId,
    DeliveryStatus deliveryStatus,
    LocalDateTime deliveryStartedAt
) {

    public static DeliveryStatusUpdateResult from(Delivery delivery) {
        return new DeliveryStatusUpdateResult(
            delivery.getRider().getId(),
            delivery.getId(),
            delivery.getDeliveryStatus(),
            LocalDateTime.ofInstant(delivery.getDeliveryStartedAt(), ZoneId.of("Asia/Seoul"))
        );
    }

}
