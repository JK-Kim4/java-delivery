package com.tutomato.delivery.interfaces.delivery.dto;

import com.tutomato.delivery.application.delivery.dto.DeliveryAddressModifyCommand;
import com.tutomato.delivery.domain.delivery.Address;

public record DeliveryAddressModifyRequest(
        Address newAddress
) {

    public DeliveryAddressModifyCommand toCommand(Long deliveryId, Long storeMemberId) {
        return new DeliveryAddressModifyCommand(deliveryId, storeMemberId, newAddress);
    }

}
