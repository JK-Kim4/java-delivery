package com.tutomato.delivery.interfaces.delivery.dto;

import com.tutomato.delivery.application.delivery.dto.DeliveryStatusUpdateResult;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Schema(description = "배달 상태 변경 결과 응답")
public record DeliveryStatusUpdateResponse(

    @Schema(
        description = "배달을 수행하는 라이더 회원 고유번호",
        example = "4001"
    )
    Long riderMemberId,

    @Schema(
        description = "상태가 변경된 배달 고유번호",
        example = "3001"
    )
    Long deliveryId,

    @Schema(
        description = "변경된 배달 상태",
        implementation = DeliveryStatus.class
    )
    DeliveryStatus deliveryStatus,

    @Schema(
        description = """
                배달이 라이더에게 할당된 시각
            """,
        example = "2025-11-15T12:40:00",
        nullable = true
    )
    LocalDateTime allocatedAt,

    @Schema(
        description = """
            배달이 시작된 시각 (배송 중(IN_DELIVERY) 상태가 된 시점).
            """,
        example = "2025-11-15T12:40:00",
        nullable = true
    )
    LocalDateTime deliveryStartedAt,

    @Schema(
        description = """
            배달이 완료된 시각
            """,
        example = "2025-11-15T12:40:00",
        nullable = true
    )
    LocalDateTime completedAt
) {

    public static DeliveryStatusUpdateResponse from(DeliveryStatusUpdateResult result) {
        return new DeliveryStatusUpdateResponse(
            result.riderMemberId(),
            result.deliveryId(),
            result.deliveryStatus(),
            LocalDateTime.ofInstant(result.allocatedAt(), ZoneId.of("Asia/Seoul")),
            LocalDateTime.ofInstant(result.deliveryStartedAt(), ZoneId.of("Asia/Seoul")),
            LocalDateTime.ofInstant(result.completedAt(), ZoneId.of("Asia/Seoul"))
        );
    }

}
