package com.tutomato.delivery.interfaces.delivery.dto;

import com.tutomato.delivery.application.delivery.dto.DeliverySearchResult;
import com.tutomato.delivery.domain.delivery.Address;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "배달 조회 결과 응답")
public record DeliverySearchResponse(

    @Schema(
        description = "배달 고유번호",
        example = "3001"
    )
    Long deliveryId,

    @Schema(
        description = "배달이 연결된 주문 고유번호",
        example = "1001"
    )
    Long orderId,

    @Schema(
        description = "주문을 접수한 가게(Store) 회원 고유번호",
        example = "2001"
    )
    Long storeId,

    @Schema(
        description = "주문을 접수한 가게(Store) 이름",
        example = "BBQ 치킨 강남점"
    )
    String storeName,

    @Schema(
        description = "해당 배달을 할당받은 라이더 회원 고유번호 (REQUESTED 상태 등 라이더 미할당 시 null)",
        example = "4001",
        nullable = true
    )
    Long allocatedRiderId,

    @Schema(
        description = "해당 배달을 할당받은 라이더 이름 (라이더 미할당 시 null)",
        example = "홍길동",
        nullable = true
    )
    String allocatedRiderName,

    @Schema(
        description = "배달 도착지 주소 정보",
        implementation = Address.class
    )
    Address destination,

    @Schema(
        description = "배달 현재 상태",
        implementation = DeliveryStatus.class
    )
    DeliveryStatus deliveryStatus,

    @Schema(
        description = "배달 엔티티가 생성된 시각 (서버 기준, KST 변환 값)",
        example = "2025-11-15T12:34:56"
    )
    LocalDateTime createdAt,

    @Schema(
        description = "배달 요청 시각 (고객 주문으로부터 배달 요청이 생성된 시각)",
        example = "2025-11-15T12:30:00"
    )
    LocalDateTime requestedAt,

    @Schema(
        description = "라이더에게 배달이 할당된 시각",
        example = "2025-11-15T12:32:00",
        nullable = true
    )
    LocalDateTime allocatedAt,

    @Schema(
        description = "라이더가 픽업을 완료하고 배송을 시작한 시각",
        example = "2025-11-15T12:40:00",
        nullable = true
    )
    LocalDateTime deliveryStartedAt,

    @Schema(
        description = "배달이 완료된 시각",
        example = "2025-11-15T13:05:00",
        nullable = true
    )
    LocalDateTime completedAt
) {

    public static DeliverySearchResponse from(DeliverySearchResult delivery) {
        return new DeliverySearchResponse(
            delivery.deliveryId(),
            delivery.orderId(),
            delivery.storeId(),
            delivery.storeName(),
            delivery.allocatedRiderId(),
            delivery.allocatedRiderName(),
            delivery.destination(),
            delivery.deliveryStatus(),
            toLocalDateTimeOrNull(delivery.createdAt()),
            toLocalDateTimeOrNull(delivery.requestedAt()),
            toLocalDateTimeOrNull(delivery.allocatedAt()),
            toLocalDateTimeOrNull(delivery.deliveryStartedAt()),
            toLocalDateTimeOrNull(delivery.completedAt())
        );
    }

    public static List<DeliverySearchResponse> fromList(List<DeliverySearchResult> deliveries) {
        return deliveries.stream().map(DeliverySearchResponse::from)
            .collect(Collectors.toList());
    }

    private static LocalDateTime toLocalDateTimeOrNull(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
    }
}
