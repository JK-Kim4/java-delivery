package com.tutomato.delivery.application.delivery.dto;

import com.tutomato.delivery.domain.delivery.Address;
import com.tutomato.delivery.domain.delivery.Delivery;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import com.tutomato.delivery.domain.member.Member;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


public record DeliveryAllocateResult(
    Long riderMemberId,
    String riderName,
    Long orderId,
    Long deliveryId,
    Address destination,
    DeliveryStatus deliveryStatus,
    Instant allocatedAt
) {

    public static DeliveryAllocateResult of(
        Member member,
        Delivery delivery
    ) {
        return new DeliveryAllocateResult(
            member.getId(),
            member.getName(),
            delivery.getOrderId(),
            delivery.getId(),
            delivery.getDestinationAddress(),
            delivery.getDeliveryStatus(),
            delivery.getAllocatedAt()
        );
    }

}
