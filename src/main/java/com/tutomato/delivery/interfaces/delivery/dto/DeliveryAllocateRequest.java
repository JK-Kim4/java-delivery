package com.tutomato.delivery.interfaces.delivery.dto;

import com.tutomato.delivery.application.delivery.dto.DeliveryAllocateCommand;
import com.tutomato.delivery.common.extractor.AuthMemberRequest;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "배달 할당(라이더 수락) 요청 바디"
)
public record DeliveryAllocateRequest(

    @Schema(
        description = "할당(수락)할 배달 고유번호",
        example = "1001",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Long deliveryId
) {
    public DeliveryAllocateCommand toCommand(AuthMemberRequest authMember) {
        return new DeliveryAllocateCommand(authMember.id(), deliveryId);
    }
}
