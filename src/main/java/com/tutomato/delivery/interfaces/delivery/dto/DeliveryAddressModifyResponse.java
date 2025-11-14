package com.tutomato.delivery.interfaces.delivery.dto;

import com.tutomato.delivery.application.delivery.dto.DeliveryAddressModifyResult;
import com.tutomato.delivery.domain.delivery.Address;

public record DeliveryAddressModifyResponse(
        Long deliveryId,
        Address updatedAddress
) {

    public static DeliveryAddressModifyResponse from(DeliveryAddressModifyResult result) {
        return new DeliveryAddressModifyResponse(result.deliveryId(), result.updatedAddress());
    }

}
