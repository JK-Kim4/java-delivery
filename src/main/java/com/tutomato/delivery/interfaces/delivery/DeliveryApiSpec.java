package com.tutomato.delivery.interfaces.delivery;

import com.tutomato.delivery.application.delivery.dto.DeliveryAddressModifyResult;
import com.tutomato.delivery.common.extractor.AuthMemberRequest;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryAddressModifyRequest;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryAddressModifyResponse;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryAllocateRequest;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryAllocateResponse;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryStatusUpdateRequest;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryStatusUpdateResponse;
import org.springframework.http.ResponseEntity;

public interface DeliveryApiSpec {

    ResponseEntity<DeliveryAllocateResponse> allocate(
        AuthMemberRequest authMember,
        DeliveryAllocateRequest request
    );

    ResponseEntity<DeliveryStatusUpdateResponse> startDelivery(
        AuthMemberRequest authMember,
        DeliveryStatusUpdateRequest request
    );

    ResponseEntity<DeliveryStatusUpdateResponse> completeDelivery(
        AuthMemberRequest authMember,
        DeliveryStatusUpdateRequest request
    );

    ResponseEntity<DeliveryAddressModifyResponse> modifyAddress(
        AuthMemberRequest authMember,
        DeliveryAddressModifyRequest request
    );
}
