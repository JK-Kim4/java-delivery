package com.tutomato.delivery.application.delivery.dto;

import com.tutomato.delivery.domain.delivery.Delivery;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import java.time.Instant;


public record DeliveryStatusUpdateResult(
    Long riderMemberId,
    Long deliveryId,
    DeliveryStatus deliveryStatus,
    Instant allocatedAt,
    Instant deliveryStartedAt,
    Instant completedAt
) {

    public static DeliveryStatusUpdateResult from(Delivery delivery) {
        return new DeliveryStatusUpdateResult(
            delivery.getRider().getId(),
            delivery.getId(),
            delivery.getDeliveryStatus(),
            delivery.getAllocatedAt(),
            delivery.getDeliveryStartedAt(),
            delivery.getCompletedAt()
        );
    }

}
