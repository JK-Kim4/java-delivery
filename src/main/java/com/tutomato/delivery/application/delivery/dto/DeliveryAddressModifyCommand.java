package com.tutomato.delivery.application.delivery.dto;

import com.tutomato.delivery.common.extractor.AuthMemberRequest;
import com.tutomato.delivery.domain.delivery.Address;

public record DeliveryAddressModifyCommand(
        Long deliveryId,
        Long storeMemberId,
        Address newAddress
) {
    public static DeliveryAddressModifyCommand of(Long deliveryId, AuthMemberRequest request, Address newAddress) {
        return new DeliveryAddressModifyCommand(deliveryId, request.id(), newAddress);
    }

}
