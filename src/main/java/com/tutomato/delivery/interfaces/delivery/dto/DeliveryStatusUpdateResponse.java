package com.tutomato.delivery.interfaces.delivery.dto;

import com.tutomato.delivery.application.delivery.dto.DeliveryStatusUpdateResult;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;

import java.time.LocalDateTime;

public record DeliveryStatusUpdateResponse(
        Long riderMemberId,
        Long deliveryId,
        DeliveryStatus deliveryStatus,
        LocalDateTime deliveryStartedAt
) {

    public static DeliveryStatusUpdateResponse from(DeliveryStatusUpdateResult result) {
        return new DeliveryStatusUpdateResponse(
                result.riderMemberId(),
                result.deliveryId(),
                result.deliveryStatus(),
                result.deliveryStartedAt()
        );
    }

}
