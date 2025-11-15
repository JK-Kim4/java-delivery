package com.tutomato.delivery.interfaces.delivery;

import com.tutomato.delivery.application.delivery.DeliveryAllocateService;
import com.tutomato.delivery.application.delivery.DeliveryQueryService;
import com.tutomato.delivery.application.delivery.DeliveryUpdateService;
import com.tutomato.delivery.application.delivery.dto.*;
import com.tutomato.delivery.common.annotation.FromAuthHeader;
import com.tutomato.delivery.common.annotation.RiderOnly;
import com.tutomato.delivery.common.annotation.StoreOnly;
import com.tutomato.delivery.common.extractor.AuthMemberRequest;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import com.tutomato.delivery.interfaces.ApiResponse;
import com.tutomato.delivery.interfaces.delivery.dto.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryApiController implements DeliveryApiSpec {

    private final DeliveryQueryService deliveryQueryService;
    private final DeliveryUpdateService deliveryUpdateService;
    private final DeliveryAllocateService deliveryAllocateService;

    public DeliveryApiController(
        DeliveryQueryService deliveryQueryService,
        DeliveryUpdateService deliveryUpdateService,
        DeliveryAllocateService deliveryAllocateService
    ) {
        this.deliveryQueryService = deliveryQueryService;
        this.deliveryUpdateService = deliveryUpdateService;
        this.deliveryAllocateService = deliveryAllocateService;
    }

    @Override
    @RiderOnly
    @PatchMapping("/allocate/{deliveryId}")
    public ResponseEntity<ApiResponse<DeliveryAllocateResponse>> allocate(
        @FromAuthHeader AuthMemberRequest authMember,
        @PathVariable(name = "deliveryId") Long deliveryId
    ) {
        DeliveryAllocateResult result = deliveryAllocateService.allocate(
            DeliveryAllocateCommand.of(authMember, deliveryId)
        );

        return ResponseEntity.ok(
            ApiResponse.success(
                "라이더 할당 성공",
                DeliveryAllocateResponse.from(result)
            )
        );
    }

    @Override
    @StoreOnly
    @PatchMapping("/start/{deliveryId}")
    public ResponseEntity<ApiResponse<DeliveryStatusUpdateResponse>> startDelivery(
        @FromAuthHeader AuthMemberRequest authMember,
        @PathVariable(name = "deliveryId") Long deliveryId
    ) {
        DeliveryStatusUpdateResult result = deliveryUpdateService.startDelivery(
            DeliveryStatusUpdateCommand.createWithStore(deliveryId, authMember)
        );

        return ResponseEntity.ok(
            ApiResponse.success(
                "배달 상태 변경 성공",
                DeliveryStatusUpdateResponse.from(result)
            )
        );
    }

    @Override
    @RiderOnly
    @PatchMapping("/complete/{deliveryId}")
    public ResponseEntity<ApiResponse<DeliveryStatusUpdateResponse>> completeDelivery(
        @FromAuthHeader AuthMemberRequest authMember,
        @PathVariable(name = "deliveryId") Long deliveryId
    ) {
        DeliveryStatusUpdateResult result = deliveryUpdateService.completeDelivery(
            DeliveryStatusUpdateCommand.createWithRider(deliveryId, authMember)
        );

        return ResponseEntity.ok(
            ApiResponse.success(
                "배달 상태 변경 성공",
                DeliveryStatusUpdateResponse.from(result)
            )
        );
    }

    @Override
    @StoreOnly
    @PatchMapping("/address/{deliveryId}")
    public ResponseEntity<ApiResponse<DeliveryAddressModifyResponse>> modifyAddress(
        @FromAuthHeader AuthMemberRequest authMember,
        @PathVariable(name = "deliveryId") Long deliveryId,
        @RequestBody DeliveryAddressModifyRequest request
    ) {
        DeliveryAddressModifyResult result = deliveryUpdateService.modifyAddress(
            DeliveryAddressModifyCommand.of(deliveryId, authMember, request.newAddress())
        );

        return ResponseEntity.ok(
            ApiResponse.success(
                "도착지 주소 변경 성공",
                DeliveryAddressModifyResponse.from(result)
            )
        );
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<DeliverySearchResponse>>> search(
        @FromAuthHeader AuthMemberRequest authMember,
        @RequestParam(value = "riderId", required = false) Long riderId,
        @RequestParam(value = "period", required = true) DeliverySearchPeriod period,
        @RequestParam(value = "statuses", required = false) List<DeliveryStatus> statuses
    ) {
        List<DeliverySearchResult> results = deliveryQueryService.search(
            DeliverySearchCriteria.of(riderId, period, statuses)
        );

        return ResponseEntity.ok(
            ApiResponse.success(
                "배달 조회 성공",
                DeliverySearchResponse.fromList(results)
            )
        );
    }
}
