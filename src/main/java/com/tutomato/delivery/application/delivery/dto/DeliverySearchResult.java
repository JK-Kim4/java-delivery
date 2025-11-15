package com.tutomato.delivery.application.delivery.dto;

import com.tutomato.delivery.domain.delivery.Address;
import com.tutomato.delivery.domain.delivery.Delivery;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    LocalDateTime createdAt,
    LocalDateTime requestedAt,
    LocalDateTime allocatedAt,
    LocalDateTime deliveryStartedAt,
    LocalDateTime completedAt
) {

    public static DeliverySearchResult from(Delivery delivery) {
        return new DeliverySearchResult(
            delivery.getId(),
            delivery.getOrderId(),
            delivery.getOrder().getStore().getId(),
            delivery.getOrder().getStore().getName(),
            delivery.getRider().getId(),
            delivery.getRider().getName(),
            delivery.getDestinationAddress(),
            delivery.getDeliveryStatus(),
            toLocalDateTimeOrNull(delivery.getCreatedAt()),
            toLocalDateTimeOrNull(delivery.getRequestedAt()),
            toLocalDateTimeOrNull(delivery.getAllocatedAt()),
            toLocalDateTimeOrNull(delivery.getDeliveryStartedAt()),
            toLocalDateTimeOrNull(delivery.getCompletedAt())
        );
    }

    public static List<DeliverySearchResult> fromList(List<Delivery> deliveries) {
        return deliveries.stream().map(DeliverySearchResult::from)
            .collect(Collectors.toList());
    }

    private static LocalDateTime toLocalDateTimeOrNull(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
    }

}
