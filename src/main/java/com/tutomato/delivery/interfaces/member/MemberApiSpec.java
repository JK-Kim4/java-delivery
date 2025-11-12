package com.tutomato.delivery.interfaces.member;

import com.tutomato.delivery.interfaces.ApiResponse;
import com.tutomato.delivery.interfaces.member.dto.MemberAuthenticateRequest;
import com.tutomato.delivery.interfaces.member.dto.MemberAuthenticateResponse;
import com.tutomato.delivery.interfaces.member.dto.MemberRegisterRequest;
import com.tutomato.delivery.interfaces.member.dto.MemberRegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
    name = "Member",
    description = "회원 가입, 로그인 등 회원 정보 관리를 위한 API"
)
public interface MemberApiSpec {

    @Operation(
        summary = "회원 가입",
        description = """
            회원 가입을 위한 API입니다.
            
            요청 시 다음 정보를 Request Body에 포함하여 전달해야 합니다.
            
            - **ID**: 서비스에서 사용할 로그인 ID
            - **비밀번호**: 아래 규칙을 만족해야 합니다.
              - 길이: **12자 이상**
              - 구성: 영어 대문자(A–Z), 영어 소문자(a–z), 숫자(0–9), 특수문자 중 **3종류 이상** 조합
            - **사용자 이름**: 화면에 표시될 이름
            """
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "회원 가입 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemberRegisterResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "비밀번호 규칙 불일치, 이름 규칙 위반 등, 잘못된 사용자 요청으로 인한 회원 가입 실패"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "이미 등록된 계정(ID)으로 인한 회원 가입 실패"
        )
    })
    ResponseEntity<ApiResponse<MemberRegisterResponse>> register(
        MemberRegisterRequest request
    );


    @Operation(
        summary = "로그인",
        description = """
            사용자로부터 ID와 비밀번호를 입력받아 로그인을 처리합니다.
            
            요청 정보:
            - **ID**: 가입 시 사용한 아이디
            - **비밀번호**: 가입 시 등록한 비밀번호
            
            동작:
            - ID와 비밀번호가 이미 가입되어 있는 회원 정보와 일치하면 로그인이 성공합니다.
            - 로그인 성공 시 **AccessToken**, **RefreshToken**을 발급하여 응답 본문(Tokens)에 포함합니다.
            
            응답:
            - **200 OK** : 로그인 성공, AccessToken / RefreshToken이 포함된 응답 반환
            - **400 Bad Request** : 잘못된 형식의 요청 (필수 값 누락 등)
            - **401 Unauthorized** : ID 또는 비밀번호가 일치하지 않는 경우
            """
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "로그인 성공 - AccessToken 및 RefreshToken 발급",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemberAuthenticateResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (요청 값 검증 실패)"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증 실패 (ID 또는 비밀번호 불일치)"
        )
    })
    ResponseEntity<ApiResponse<MemberAuthenticateResponse>> authenticate(
        MemberAuthenticateRequest request
    );

}
