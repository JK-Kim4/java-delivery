package com.tutomato.delivery.application.delivery;

import com.tutomato.delivery.domain.delivery.Address;

public record DeliveryAddressModifyCommand(
    Long deliveryId,
    Long storeMemberId,
    Address newAddress
) {

}
