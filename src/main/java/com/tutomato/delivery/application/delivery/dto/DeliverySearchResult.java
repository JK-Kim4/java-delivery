package com.tutomato.delivery.application.delivery.dto;

import com.tutomato.delivery.domain.delivery.Address;
import com.tutomato.delivery.domain.delivery.Delivery;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public record DeliverySearchResult(
    Long deliveryId,
    Long orderId,
    Long storeId,
    String storeName,
    Long allocatedRiderId,
    String allocatedRiderName,
    Address destination,
    DeliveryStatus deliveryStatus,
    Instant createdAt,
    Instant requestedAt,
    Instant allocatedAt,
    Instant deliveryStartedAt,
    Instant completedAt
) {

    public static DeliverySearchResult from(Delivery delivery) {
        Long riderId = null;
        String riderName = null;

        if (delivery.getRider() != null) {
            riderId = delivery.getRider().getId();
            riderName = delivery.getRider().getName();
        }

        return new DeliverySearchResult(
            delivery.getId(),
            delivery.getOrderId(),
            delivery.getOrder().getStore().getId(),
            delivery.getOrder().getStore().getName(),
            riderId,
            riderName,
            delivery.getDestinationAddress(),
            delivery.getDeliveryStatus(),
            delivery.getCreatedAt(),
            delivery.getRequestedAt(),
            delivery.getAllocatedAt(),
            delivery.getDeliveryStartedAt(),
            delivery.getCompletedAt()
        );
    }

    public static List<DeliverySearchResult> fromList(List<Delivery> deliveries) {
        return deliveries.stream().map(DeliverySearchResult::from)
            .collect(Collectors.toList());
    }
}
