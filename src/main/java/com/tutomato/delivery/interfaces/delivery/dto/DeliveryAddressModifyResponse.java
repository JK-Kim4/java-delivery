package com.tutomato.delivery.interfaces.delivery.dto;

import com.tutomato.delivery.application.delivery.dto.DeliveryAddressModifyResult;
import com.tutomato.delivery.domain.delivery.Address;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "배달 도착지 주소 변경 결과 응답"
)
public record DeliveryAddressModifyResponse(

    @Schema(
        description = "도착지 주소가 변경된 배달 고유번호",
        example = "1001"
    )
    Long deliveryId,

    @Schema(
        description = "변경된 도착지 주소 정보",
        implementation = Address.class
    )
    Address updatedAddress
) {

    public static DeliveryAddressModifyResponse from(DeliveryAddressModifyResult result) {
        return new DeliveryAddressModifyResponse(result.deliveryId(), result.updatedAddress());
    }

}
