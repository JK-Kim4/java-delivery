package com.tutomato.delivery.interfaces.delivery;

import com.tutomato.delivery.common.extractor.AuthMemberRequest;
import com.tutomato.delivery.domain.delivery.DeliveryStatus;
import com.tutomato.delivery.interfaces.ApiResponse;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryAddressModifyRequest;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryAddressModifyResponse;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryAllocateResponse;
import com.tutomato.delivery.interfaces.delivery.dto.DeliverySearchPeriod;
import com.tutomato.delivery.interfaces.delivery.dto.DeliverySearchResponse;
import com.tutomato.delivery.interfaces.delivery.dto.DeliveryStatusUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(
    name = "Delivery",
    description = "배달 조회, 상태변경, 도착지 변경 관리를 위한 API"
)
public interface DeliveryApiSpec {

    @Operation(
        summary = "배달 할당(라이더 수락)",
        description = """
            라이더가 특정 배달 요청을 수락하여 자신에게 할당하는 API입니다.
            
            다음 조건을 만족하는 경우에만 배달 할당이 가능합니다.
            
            - 배달 상태가 `REQUESTED` 인 경우에만 라이더에게 할당할 수 있습니다.
            - 요청을 보내는 사용자는 **라이더(Rider) 계정**이어야 합니다.
            - 이미 다른 라이더에게 할당된 배달은 다시 할당할 수 없습니다.
            
            요청 시 다음 정보를 전달해야 합니다.
            
            - **deliveryId** *(필수, Request Body 또는 Query Parameter)*
              할당을 시도할 배달의 고유번호
            
            또한, 요청 헤더에 라이더 정보가 포함된 Access Token을 Authorization 필드에 포함하여 인증 정보를 전달해야 합니다.
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
            responseCode = "400",
            description = "잘못된 요청 (잘못된 형식의 파라미터, 필수 파라미터 누락 등)"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "권한이 없는 사용자 요청"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "고유번호에 해당하는 배달 혹은 Rider/Store 정보가 존재하지 않음"
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
            라이더가 픽업을 완료한 배달을 대상으로 상태를 '배송 중(IN_DELIVERY)'으로 변경하는 API입니다.
            
            다음 조건을 만족하는 경우에만 배달 시작 상태 변경이 가능합니다.
            
            - 배달 상태가 `ASSIGNED` 인 경우에만 '배송 중'으로 변경할 수 있습니다.
            - 요청을 보내는 사용자는 **해당 배달을 요청한 STORE**여야 합니다.
            
            요청 시 다음 정보를 전달해야 합니다.
            
            - **deliveryId** *(필수, PathVariable)*
              '배송 중' 상태로 변경할 배달의 고유번호
            
            또한, 요청 헤더에 배달 요청한 Store 정보가 포함된 Access Token을 Authorization 필드에 포함하여 인증 정보를 전달해야 합니다.
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
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (잘못된 형식의 파라미터, 필수 파라미터 누락 등)"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "권한이 없는 사용자 요청"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "고유번호에 해당하는 배달 혹은 Rider/Store 정보가 존재하지 않음"
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
            배송이 완료된 배달을 대상으로 상태를 '배송 완료(COMPLETED)'로 변경하는 API입니다.
            
            다음 조건을 만족하는 경우에만 배달 완료 상태 변경이 가능합니다.
            
            - 배달 상태가 `IN_DELIVERY` 인 경우에만 '배송 완료'로 변경할 수 있습니다.
            - 요청을 보내는 사용자는 **해당 배달에 할당된 라이더**여야 합니다.
            
            요청 시 다음 정보를 전달해야 합니다.
            
            - **deliveryId** *(필수, PathVariable)*  
              '배송 완료' 상태로 변경할 배달의 고유번호
            
            또한, 요청 헤더에 할당된 Rider 정보가 포함된 Access Token을 Authorization 필드에 포함하여 인증 정보를 전달해야 합니다.
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
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (잘못된 형식의 파라미터, 필수 파라미터 누락 등)"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "권한이 없는 사용자 요청"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "고유번호에 해당하는 배달 혹은 Rider/Store 정보가 존재하지 않음"
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
            배송이 아직 시작되지 않은 배달에 대해 도착지 주소를 변경하는 API입니다.
            
            다음 조건을 모두 만족하는 경우에만 도착지 변경이 가능합니다.
            
            - 배달 상태가 `REQUESTED` 또는 `ASSIGNED` 인 경우에만 변경 가능
              (이미 `IN_DELIVERY` 또는 `COMPLETED` 상태인 배달은 변경 불가)
            - 요청을 보내는 사용자는 **해당 주문을 접수한 Store 계정**이어야 합니다.
            
            요청 시 다음 정보를 전달해야 합니다.
            
            - **deliveryId** *(필수, Path Variable)*
              도착지를 변경할 배달의 고유번호
            
            - **DeliveryAddressModifyRequest** *(필수, Request Body)*
              변경할 도착지 주소 정보 (예: 우편번호, 도로명 주소, 상세 주소 등)
            
            또한, 요청 헤더에 주문을 접수한 Store 정보가 포함된 Access Token을 Authorization 필드에 포함하여 인증 정보를 전달해야 합니다.
            예) `Authorization: Bearer {accessToken}`
            """
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "도착지 주소 변경 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DeliveryStatusUpdateResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (변경 불가능 상태, 잘못된 형식의 파라미터, 필수 파라미터 누락 등)"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "권한이 없는 사용자 요청"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "고유번호에 해당하는 배달 혹은 Rider/Store 정보가 존재하지 않음"
        )
    })
    @SecurityRequirement(name = "Authorization")
    ResponseEntity<ApiResponse<DeliveryAddressModifyResponse>> modifyAddress(
        @Schema(hidden = true) AuthMemberRequest authMember,
        Long deliveryId,
        DeliveryAddressModifyRequest request
    );

    @Operation(
        summary = "배달 목록 조회",
        description = """
            라이더 또는 스토어 관점에서 기간 내 배달 목록을 조회하는 API입니다.
            
            요청 시 다음 정보를 Request Parameter로 전달해야 합니다.
            
            - **period** *(필수)* : 조회 기준 기간
              예) `M30`(30분), `H1`(1시간), `H3`(3시간), `H6`(6시간), `H12`(12시간), `D1`(1일), `D2`(2일), `D3`(3일)  
              최대 조회 가능 기간은 3일입니다.
            
            - **riderId** *(선택)* : 특정 라이더에게 할당된 배달만 조회하고자 할 때 사용하는 Rider 고유번호  
              - 미전달 또는 `null`인 경우: 라이더와 무관하게 전체 배달 목록 조회  
              - 값이 있는 경우: 해당 라이더에게 할당된 배달만 조회합니다. (REQUESTED 상태처럼 라이더 미할당 배달은 제외)
            
            - **statuses** *(선택)* : 조회할 배달 상태 목록  
              - 예) `statuses=REQUESTED,ASSIGNED,IN_DELIVERY`  
              - 생략 또는 빈 목록인 경우: 모든 상태를 대상으로 조회합니다.
            
            또한, 요청 헤더의 인증된 사용자 정보가 포함된 Access Token을 Authorization 필드에 포함하여 인증 정보를 전달해야 합니다.  
            예) `Authorization: Bearer {accessToken}`
            """
    )
    @SecurityRequirement(name = "Authorization")
    ResponseEntity<ApiResponse<List<DeliverySearchResponse>>> search(
        @Schema(hidden = true) AuthMemberRequest authMember,
        Long riderId,
        DeliverySearchPeriod period,
        @Parameter(
            description = """
                조회할 배달 상태 목록입니다.
                - 예) statuses=REQUESTED,ASSIGNED,IN_DELIVERY
                - 비워두면(미전달 또는 빈 배열) 모든 상태를 대상으로 조회합니다.
                """,
            array = @ArraySchema(
                schema = @Schema(
                    implementation = String.class,
                    allowableValues = {"REQUESTED", "ASSIGNED", "IN_DELIVERY", "COMPLETED", "CANCELED"}
                )
            )
        )
        List<DeliveryStatus> statuses
    );
}
