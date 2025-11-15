package com.tutomato.delivery.interfaces.delivery.dto;

import com.tutomato.delivery.application.delivery.dto.DeliveryAllocateCommand;
import com.tutomato.delivery.common.extractor.AuthMemberRequest;

public record DeliveryAllocateRequest(
    Long deliveryId
) {

    public DeliveryAllocateCommand toCommand(AuthMemberRequest authMember) {
        return new DeliveryAllocateCommand(authMember.id(), deliveryId);
    }
}
