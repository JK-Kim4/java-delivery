package com.tutomato.delivery.application.delivery.dto;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public record DeliveryAllocateCommand(
    Long riderMemberId,
    Long deliveryId
) {

}
