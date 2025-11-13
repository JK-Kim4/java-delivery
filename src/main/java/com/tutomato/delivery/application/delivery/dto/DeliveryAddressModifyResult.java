package com.tutomato.delivery.application.delivery.dto;

import com.tutomato.delivery.domain.delivery.Address;
import com.tutomato.delivery.domain.delivery.Delivery;

public record DeliveryAddressModifyResult(
    Long deliveryId,
    Address updatedAddress
) {

    public static DeliveryAddressModifyResult from(Delivery delivery) {
        return new DeliveryAddressModifyResult(
            delivery.getId(),
            delivery.getDestinationAddress()
        );
    }
}
