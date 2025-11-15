package com.tutomato.delivery.interfaces.delivery.dto;

import com.tutomato.delivery.application.delivery.dto.DeliveryAddressModifyCommand;
import com.tutomato.delivery.domain.delivery.Address;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(
    description = "배달 도착지 주소 변경 요청"
)
public record DeliveryAddressModifyRequest(

    @Schema(
        description = "변경할 도착지 주소 정보",
        implementation = Address.class,
        example = "{\"zipCode\":\"06236\",\"address1\":\"서울 강남구 테헤란로 123\",\"address2\":\"101호\"}"
    )
    Address newAddress
) {

    public DeliveryAddressModifyCommand toCommand(Long deliveryId, Long storeMemberId) {
        return new DeliveryAddressModifyCommand(deliveryId, storeMemberId, newAddress);
    }

}
