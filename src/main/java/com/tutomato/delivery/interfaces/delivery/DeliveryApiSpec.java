package com.tutomato.delivery.interfaces.delivery;

import com.tutomato.delivery.common.extractor.AuthMemberRequest;
import com.tutomato.delivery.interfaces.ApiResponse;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryAddressModifyRequest;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryAddressModifyResponse;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryAllocateResponse;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryStatusUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "Delivery",
        description = "배달 조회, 상태변경, 도착지 변경 관리를 위한 API"
)
public interface DeliveryApiSpec {

    @Operation(
            summary = "배달 할당",
            description = """
            라이더에게 배달할당을 위한 API입니다.
            
            요청 시 다음 정보를 Request Body에 포함하여 전달해야 합니다.
            
            - **deliveryId**: 라이더에게 할당할 배달 고유번호
            
            또한, 요청 헤더의 배차 받을 Rider 정보가 포함된 Access Token을 Authorization 필드에 포함하여 인증 정보를 전달해야 합니다.
            예) `Authorization: Bearer {accessToken}`
            """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "할당 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DeliveryAllocateResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "권한이 없는 사용자 요청"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "고유번호에 해당하는 배달 혹은 Rider 정보가 존재하지 않음"
            )
    })
    @SecurityRequirement(name = "Authorization")
    ResponseEntity<ApiResponse<DeliveryAllocateResponse>> allocate(
        @Schema(hidden = true) AuthMemberRequest authMember,
        Long deliveryId
    );

    @Operation(
            summary = "배달 시작 상태 변경",
            description = """
            라이더에게 픽업이 완료된 배달을 대상으로 상태를 '배송 중' 으로 변경합니다.
            
            요청 시 다음 정보를 Request Body에 포함하여 전달해야 합니다.
            
            - **deliveryId**: 라이더에게 할당할 배달 고유번호
            
            또한, 요청 헤더의 주문 받은 Store 정보가 포함된 Access Token을 Authorization 필드에 포함하여 인증 정보를 전달해야 합니다.
            예) `Authorization: Bearer {accessToken}`
            """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "배송 상태 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DeliveryStatusUpdateResponse.class)
                    )
            )
    })
    @SecurityRequirement(name = "Authorization")
    ResponseEntity<ApiResponse<DeliveryStatusUpdateResponse>> startDelivery(
        @Schema(hidden = true) AuthMemberRequest authMember,
        Long deliveryId
    );

    @Operation(
            summary = "배달 완료 상태 변경",
            description = """
            배송이 완료된 배달을 대상으로 '배송 완료'로 상태를 변경합니다.
            
            요청 시 다음 정보를 Request Body에 포함하여 전달해야 합니다.
            
            - **deliveryId**: 라이더에게 할당할 배달 고유번호
            
            또한, 요청 헤더의 배차 받을 Rider 정보가 포함된 Access Token을 Authorization 필드에 포함하여 인증 정보를 전달해야 합니다.
            예) `Authorization: Bearer {accessToken}`
            """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "배송 상태 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DeliveryStatusUpdateResponse.class)
                    )
            )
    })
    @SecurityRequirement(name = "Authorization")
    ResponseEntity<ApiResponse<DeliveryStatusUpdateResponse>> completeDelivery(
        @Schema(hidden = true) AuthMemberRequest authMember,
        Long deliveryId
    );


    @Operation(
            summary = "배달 도착지 주소 변경",
            description = """
            배송이 시작되지 않은 배달을 대상으로 도착지를 변경합니다.
            
            요청 시 다음 정보를 Request Body에 포함하여 전달해야 합니다.
            
            - **newAddress**: 변경될 도착지의 새 주소
            
            또한, 요청 헤더의 주문 받은 Store 정보가 포함된 Access Token을 Authorization 필드에 포함하여 인증 정보를 전달해야 합니다.
            예) `Authorization: Bearer {accessToken}`
            """
    )
    @SecurityRequirement(name = "Authorization")
    ResponseEntity<ApiResponse<DeliveryAddressModifyResponse>> modifyAddress(
        @Schema(hidden = true) AuthMemberRequest authMember,
        Long deliveryId,
        DeliveryAddressModifyRequest request
    );
}
