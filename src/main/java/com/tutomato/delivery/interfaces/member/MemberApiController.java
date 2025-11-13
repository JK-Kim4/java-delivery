package com.tutomato.delivery.interfaces.member;

import com.tutomato.delivery.application.member.MemberAuthenticateService;
import com.tutomato.delivery.application.member.MemberRegisterService;
import com.tutomato.delivery.application.member.dto.MemberAuthenticateResult;
import com.tutomato.delivery.application.member.dto.MemberRegisterResult;
import com.tutomato.delivery.interfaces.ApiResponse;
import com.tutomato.delivery.interfaces.member.dto.MemberAuthenticateRequest;
import com.tutomato.delivery.interfaces.member.dto.MemberAuthenticateResponse;
import com.tutomato.delivery.interfaces.member.dto.MemberRegisterRequest;
import com.tutomato.delivery.interfaces.member.dto.MemberRegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberApiController implements MemberApiSpec {

    private final MemberRegisterService memberRegisterService;
    private final MemberAuthenticateService memberAuthenticateService;

    public MemberApiController(
        MemberRegisterService memberRegisterService,
        MemberAuthenticateService memberAuthenticateService
        ) {
        this.memberRegisterService = memberRegisterService;
        this.memberAuthenticateService = memberAuthenticateService;
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<MemberRegisterResponse>> register(
        MemberRegisterRequest request) {
        MemberRegisterResult result = memberRegisterService.register(request.toCommand());

        return ResponseEntity.ok(
            ApiResponse.success("회원 등록 성공", MemberRegisterResponse.from(result))
        );
    }

    @Override
    @PostMapping("/auth")
    public ResponseEntity<ApiResponse<MemberAuthenticateResponse>> authenticate(
        MemberAuthenticateRequest request) {
        MemberAuthenticateResult result = memberAuthenticateService.authentication(
            request.toCommand());

        return ResponseEntity.ok(
            ApiResponse.success("회원 인증 성공", MemberAuthenticateResponse.from(result))
        );
    }
}
