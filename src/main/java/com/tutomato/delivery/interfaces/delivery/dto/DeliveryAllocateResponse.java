package com.tutomato.delivery.interfaces.delivery.dto;

import com.tutomato.delivery.application.delivery.dto.DeliveryAllocateResult;
import com.tutomato.delivery.domain.delivery.Address;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Schema(description = "배달 할당(라이더 수락) 결과 응답")
public record DeliveryAllocateResponse(

    @Schema(
        description = "배달을 할당받은 라이더 회원 고유번호",
        example = "2001"
    )
    Long riderMemberId,

    @Schema(
        description = "배달을 할당받은 라이더 이름",
        example = "홍길동"
    )
    String riderName,

    @Schema(
        description = "배달이 연결된 주문 고유번호",
        example = "1001"
    )
    Long orderId,

    @Schema(
        description = "배달 고유번호",
        example = "3001"
    )
    Long deliveryId,

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
        description = "라이더에게 배달이 할당된 시각",
        example = "2025-11-15T12:34:56"
    )
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
            LocalDateTime.ofInstant(result.allocatedAt(), ZoneId.of("Asia/Seoul"))
        );
    }

}
