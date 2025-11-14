package com.tutomato.delivery.application.delivery.dto;

import com.tutomato.delivery.common.extractor.AuthMemberRequest;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public record DeliveryAllocateCommand(
    Long riderMemberId,
    Long deliveryId
) {

    public static DeliveryAllocateCommand of(AuthMemberRequest authMember, Long deliveryId) {
        return new DeliveryAllocateCommand(authMember.id(), deliveryId);
    }

}
