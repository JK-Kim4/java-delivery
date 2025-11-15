package com.tutomato.delivery.interfaces.delivery.dto;

import com.tutomato.delivery.application.delivery.dto.DeliveryAllocateResult;
import com.tutomato.delivery.domain.delivery.Address;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import java.time.LocalDateTime;

public record DeliveryAllocateResponse(
    Long riderMemberId,
    String riderName,
    Long orderId,
    Long deliveryId,
    Address destination,
    DeliveryStatus deliveryStatus,
    LocalDateTime allocatedAt
) {

    public static DeliveryAllocateResponse from(DeliveryAllocateResult result) {
        return new DeliveryAllocateResponse(
            result.riderMemberId(),
            result.riderName(),
            result.orderId(),
            result.deliveryId(),
            result.destination(),
            result.deliveryStatus(),
            result.allocatedAt()
        );
    }

}
